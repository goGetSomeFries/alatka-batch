class JobFlow {
    constructor(graphContainer, toolbarContainer) {
        this.graphContainer = document.getElementById(graphContainer);
        this.toolbarContainer = document.getElementById(toolbarContainer);
        if (!this.graphContainer) {
            throw new Error(`Container with id "${graphContainer}" not found.`);
        }
        if (!this.toolbarContainer) {
            throw new Error(`Container with id "${toolbarContainer}" not found.`);
        }
        this.init();
    };

    init() {
        if (!mxClient.isBrowserSupported()) {
            mxUtils.error('Browser is not supported!', 200, false);
            return;
        }

        this.graph = new mxGraph(this.graphContainer);
        this.configureGraph();
        this.configureToolbar();
        this.configureContextMenu();
    };

    configureGraph() {
        // 允许连线
        this.graph.setConnectable(true);
        // 不允许悬空边
        this.graph.setAllowDanglingEdges(false);
        // 不允许调整大小
        this.graph.setCellsResizable(false);
        // DAG
        this.graph.setMultigraph(false);
        // 通过拖拽选择多个元素
        new mxRubberband(this.graph);
    };

    configureToolbar() {
        const editor = new mxEditor();
        editor.setGraph(this.graph);
        const toolbar = new mxDefaultToolbar(this.toolbarContainer, editor);
        toolbar.addItem('测试', null, 'copy', null);
    }

    // 自定义右键菜单
    configureContextMenu() {
    }
}