const branchModal = new bootstrap.Modal(
  document.getElementById('branchModal')
);

const productModal = new bootstrap.Modal(
  document.getElementById('productModal')
);

export function showBranchModal() {
  branchModal.show();
}

export function showProductModal() {
  productModal.show();
}
