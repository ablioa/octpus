var coreModel = {
    id: '',
    rootNode:{},
    model: [],
    modelMap: {},
    parentMap: {}
};

/**
 * 后端数据服务接口
 * @returns 服务接口集合
 */
function DataService(){
    const url = "http://localhost:8000/model/catalog";

    /**
     * 构建模型树
     * @param {*} modelMap
     * @param {*} parentMap
     * @param {*} node 
     * @param {*} parentNode 
     */
    function buildTreeModel(nodeMap,parentMap,node, parentNode) {
        var nodeId         = node.uuid;

        nodeMap[nodeId]  = node;
        parentMap[nodeId] = parentNode;

        if(node.collection){
            node.name = node.name + '[]';
        }

        /**
         * 构造映射对象
         */
        let counterPart={
            name: node.name,
            domainType: node.domainType,
            method: '',
            table : '',
            groovy: ''
        };

        node.counterPart = counterPart;
        node.selected= false;

        $.each(node.fields, function (idx, child) {
            buildTreeModel(nodeMap,parentMap,child, node);
        });
    }
    
    function saveConfig(rule,callback){
        let data = {
            rule: rule
        };

        if(coreModel.id != ''){
            data.id = coreModel.id;
        }

        $.ajax({
            type: "POST",
            url: 'http://localhost:8000/rule/',
            dataType: "JSON",
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                if(coreModel.id == ''){
                    coreModel.id = response.id;
                }
            }
        });
    }

    /**
     * 取得映射模型
     * @param {数据处理回调} callback 
     */
    function getMapModel(callback){
        $.ajax({
            type: "GET",
            url: url,
            dataType: "JSON",
            success: function (data, textStatus, jqXHR) {
                let model = [];
                let nodeMap={};
                let parentMap={};
        
                model.push(data);
        
                $.each(model, function (idx, node) {
                    buildTreeModel(nodeMap,parentMap,node, null);
                });

                let coreModel = {
                    id:'',
                    rootNode: data,
                    model: model,
                    nodeMap: nodeMap,
                    parentMap: parentMap
                };

                callback(coreModel);
            }
        });
    }

    function retrieveReferencePath(node){
        let fromPath = node.name;
        let topath = node.counterPart.name;

        let parent = coreModel.parentMap[node.uuid];
        while(parent != null){
            fromPath = parent.name +'.' + fromPath;
            topath = parent.counterPart.name + '.' + topath;

            parent = coreModel.parentMap[parent.uuid];
        }

        let item = {
            mid: node.uuid,
            subject: fromPath,
            object : topath
        }

        return item;
    }

    return {
        getMapModel : getMapModel,
        retrieveReferencePath : retrieveReferencePath,
        saveConfig: saveConfig
    }
}