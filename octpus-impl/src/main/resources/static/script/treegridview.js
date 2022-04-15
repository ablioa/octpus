const lineTemplate='<td width="40%" class="from" data-container="body"> \
                        <span class="expressionText">\
                            <span class="data-node">fromName</span>\
                            <span class="biz-tag-type">/</span>\
                            <span class="data-type biz-tag-type">fromType</span>\
                        </span>\
                        </td>\
                        <td width="40%" class="to" data-container="body">\
                        <span class="expressionText">\
                        <span class="mapping-node">toName</span>\
                        <span class="biz-tag-type">/</span>\
                        <span class="mapping-type biz-tag-type">fromType</span>\
                        </span>\
                        </td>\
                        <td width="20%" class="action" style="text-align:right" data-container="body">\
                        <span class="ui-icon ui-icon-document biz-info"></span>\
                        <input style="display:inline;" type="checkbox"/>\
                    </td>';
/**
 * 构造树节点
 * @param {*} nodeModel 
 * @param {*} parent 
 * @returns 
 */
function buildLine(nodeModel, parent) {
    let parentUuid = '0'
    if (parent != null) {
        parentUuid = parent.uuid;
    }

    var $line = $('<tr data-uuid=' + nodeModel.uuid + ' data-parent-uuid=' + parentUuid + '></tr>');
    $line.data('mapModel', nodeModel);
    $line.attr('id', nodeModel.uuid);
    $line.data('model', nodeModel);

    let $lt = $(lineTemplate);
    $line.append($lt);

    $line.find('.data-node').text(nodeModel.name);
    $line.find('.data-type').text(nodeModel.domainType);
    $line.find('.mapping-node').text(nodeModel.name);
    $line.find('.mapping-type').text(nodeModel.domainType);

    return $line;
}

/**
 * 构造树节点
 * @param {*} $treeRoot 
 * @param {*} nodeModel 
 * @param {*} parentNodeId 
 */
function renderNode($treeRoot, nodeModel, parentNodeId) {
    let curNodeId = $('.tree').data('nodeId');
    $('.tree').data('nodeId', (++curNodeId));

    var parent = coreModel.parentMap[nodeModel.uuid];
    var $line = buildLine(nodeModel, parent);
    $line.addClass('treegrid-' + curNodeId);

    if (parentNodeId != null) {
        $line.addClass('treegrid-parent-' + parentNodeId);
    }

    $treeRoot.find('tbody').append($line);

    $.each(nodeModel.fields, function (idx, item) {
        renderNode($treeRoot, item, curNodeId);
    });
}