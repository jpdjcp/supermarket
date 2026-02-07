import { elements } from './../elements.js'
import { state } from './../../state/state.js'

export function updateSaleActionsUI() {
    const hasSale = !!state.currentSaleId;
    const hasItems = state.saleItems.length > 0;
    const isOpen = state.currentSaleStatus === 'OPEN';

    // "Add Product" & "Cancel Cale" buttons only if Sale exists and is OPEN
    elements.addProductBtn.toggleAttribute('disabled', !hasSale || !isOpen);
    elements.cancelSaleBtn.toggleAttribute('disabled', !hasSale || !isOpen);

    // "Finish Sale" button only if there are items
    elements.finishSaleBtn.toggleAttribute('disabled', !hasItems);
}