const API_URL = '/api';
const branchRegisterBtn = document.querySelector('#btnGuardarSucursal');
const productRegisterBtn = document.querySelector('#btnGuardarProducto');
const saleRegisterBtn = document.querySelector('#btnRegistrarVenta');
const addProductToSaleBtn = document.querySelector('#btnAgregarProducto');
var saleId = 0;

branchRegisterBtn.addEventListener('click', async () => {
    try {
        const branchAddress = document.querySelector('#direccionSucursal').value;
        const response = await fetch(API_URL + '/branch/create', {
            method: 'POST',
            headers: {
                'Content-Type' : 'application/json',
                'Accept' : 'application/json'
            },
            body: JSON.stringify({address : branchAddress})
        });

        const data = await response.json();
        console.log('Sucursal creada:', data);
        alert('Sucursal registrada exitosamente');
    } catch (error) {
        console.error('Error al registrar la sucursal:', error);
        alert('Error al registrar la sucursal: ' + error.message);
    }
});

productRegisterBtn.addEventListener('click', async () => {
    try {
        const name = document.querySelector('#nombreProducto').value;
        const price = parseFloat(document.querySelector('#precioProducto').value);

        const response = await fetch(API_URL + '/product/create', {
            method: 'POST',
            headers: {'Content-Type' : 'application/json'},
            body: JSON.stringify({name : name, price : price})
        });

        const data = await response.json();
        console.log('Producto creado:', data);
        alert('Producto registrado exitosamente');
    } catch (error) {
        console.error('Error al registrar el producto:', error);
        alert('Error al registrar el producto: ' + error.message);
    }
});

saleRegisterBtn.addEventListener('click', async () => {
    try {
        const branchId = Number(document.querySelector('#sucursalVenta').value);
        const response = await fetch(API_URL + '/sales/create/' + branchId, {
            method: 'POST',
            headers: {'Content-Type' : 'application/json'}
        });

        const data = await response.json();
        saleId = data.id;
        console.log('Venta creada:', data);
        alert('Venta registrada exitosamente');
    } catch (error) {
        console.error('Error al registrar la venta:', error);
        alert('Error al registrar la venta: ' + error.message);
    }
});

addProductToSaleBtn.addEventListener('click', async () => {
    try {
        const saleId = Number(document.querySelector('#ventaId').value);
        const productId = Number(document.querySelector('#productoId').value);
        const quantity = Number(document.querySelector('#cantidadProducto').value);
        
        const response = await fetch(API_URL + '/sales/' + saleId + '/items',
            {
                method : 'POST',
                headers : {'Content-Type' : 'application/json'},
                body : JSON.stringify(
                    {
                        'productId' : productId,
                        'quantity' : quantity
                    }
                )
            }
        );

        if (!response.ok) throw new Error('Error en la respuesta del servidor');
        console.log('Producto agregado a la venta exitosamente', response.json());
        alert('Producto agregado a la venta exitosamente');
    } catch (error) {
        console.error('Error al agregar el producto a la venta:', error);
        alert('Error al agregar el producto a la venta: ' + error.message);
    }
});