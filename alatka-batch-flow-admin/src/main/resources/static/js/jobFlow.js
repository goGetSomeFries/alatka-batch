class JobFlow {
    constructor(graphContainer, toolbarContainer) {
        if (!mxClient.isBrowserSupported()) {
            mxUtils.error('浏览器不支持 mxGraph!', 200, false);
            return;
        }

        this.graphContainer = document.getElementById(graphContainer);
        this.toolbarContainer = document.getElementById(toolbarContainer);
        if (!this.graphContainer) {
            throw new Error(`Container with id "${graphContainer}" not found.`);
        }
        if (!this.toolbarContainer) {
            throw new Error(`Container with id "${toolbarContainer}" not found.`);
        }

        this.graph = new mxGraph(this.graphContainer);
        this.#configureGraph();
        this.#configureNodeStyle();
        this.#configureContextMenu();
    };

    #configureGraph() {
        // 允许连线
        this.graph.setConnectable(true);
        // 不允许悬空边
        this.graph.setAllowDanglingEdges(false);
        // 不允许调整大小
        this.graph.setCellsResizable(false);
        // 不允许两个节点间多条线
        this.graph.setMultigraph(false);
        // 启用引导线 (Guide) 帮助对齐
        mxGraphHandler.prototype.guidesEnabled = true;
        // 通过拖拽选择多个元素
        new mxRubberband(this.graph);
    };

    #configureNodeStyle() {
        const stylesheet = this.graph.getStylesheet();
        // 基础通用样式
        const baseStyle = {
            [mxConstants.STYLE_ROUNDED]: true,
            [mxConstants.STYLE_STROKEWIDTH]: 2,
            [mxConstants.STYLE_FONTSIZE]: 16,
            [mxConstants.STYLE_FONTSTYLE]: mxConstants.FONT_BOLD,
            [mxConstants.STYLE_ALIGN]: mxConstants.ALIGN_CENTER,
            [mxConstants.STYLE_VERTICAL_ALIGN]: mxConstants.ALIGN_MIDDLE,
        };

        // Start 节点样式
        {
            const style = mxUtils.clone(baseStyle);
            style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_ELLIPSE;
            style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
            style[mxConstants.STYLE_FILLCOLOR] = '#d4edda';
            style[mxConstants.STYLE_STROKECOLOR] = '#28a745';
            style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
            stylesheet.putCellStyle('START', style);
        }

        // Step 节点样式
        {
            const style = mxUtils.clone(baseStyle);
            style[mxConstants.STYLE_FILLCOLOR] = '#cfe2ff';
            style[mxConstants.STYLE_STROKECOLOR] = '#0d6efd';
            style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
            stylesheet.putCellStyle('STEP', style);
        }

        // End 节点样式
        {
            const style = mxUtils.clone(baseStyle);
            style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_ELLIPSE;
            style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
            style[mxConstants.STYLE_FILLCOLOR] = '#f8d7da';
            style[mxConstants.STYLE_STROKECOLOR] = '#dc3545';
            style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
            stylesheet.putCellStyle('END', style);
        }
    }

    // 自定义右键菜单
    #configureContextMenu() {
    }

    addStartNode(x, y) {
        this.#addNode('START', 'Start', x, y, 70, 70);
    };

    addEndNode(x, y) {
        this.#addNode('END', 'End', x, y, 70, 70);
    };

    #addNode(type, label, x, y, w, h) {
        const parent = this.graph.getDefaultParent();
        this.graph.getModel().beginUpdate();
        try {
            return this.graph.insertVertex(parent, null, label, x, y, w, h, type);
        } finally {
            this.graph.getModel().endUpdate();
        }
    }
}