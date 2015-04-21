function adjustUnitOfMeasurement(e)
{
    var parentTableNode = getAncestorByTagName(e, 'table');
    var nearestLabel = parentTableNode.getElementsByClassName('numericValueMeasurementUnit')[0];
    if (e.value === 'resp_time_per_page')
    {
        nearestLabel.innerHTML = ' ms';
    }
    else if (e.value === 'failed_loops')
    {
        nearestLabel.innerHTML = ' %'
    }
}

function getAncestorByTagName(el, tn) {
    tn = tn.toLowerCase();
    if (el.parentNode) {
        if (el.parentNode.nodeType == 1
                && el.parentNode.tagName.toLowerCase() == tn
                )
            return el.parentNode;
        return getAncestorByTagName(el.parentNode, tn);
    }
    return null
}