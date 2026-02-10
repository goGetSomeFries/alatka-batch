class JobFlow {

    static NODE_START = 'START';
    static NODE_END = 'END';
    static NODE_DECISION = 'DECISION';
    static NODE_STEP = 'STEP';

    constructor(graphContainer) {
        if (!mxClient.isBrowserSupported()) {
            mxUtils.error('浏览器不支持 mxGraph!', 200, false);
            return;
        }

        this.graphContainer = document.getElementById(graphContainer);
        if (!this.graphContainer) {
            throw new Error(`Container with id "${graphContainer}" not found.`);
        }

        this.graph = new mxGraph(this.graphContainer);
        this.shapeProps = {};
        this.#configureGraph();
        this.#configureShortcuts();
        this.#configureNodeStyle();
        this.#configureContextMenu();
        this.#validConnection();
    };

    #configureGraph() {
        // 只允许连线编辑文字
        this.graph.isCellEditable = function (cell) {
            return this.getModel().isEdge(cell);
        };
        // 允许连线
        this.graph.setConnectable(true);
        // 不允许悬空边
        this.graph.setAllowDanglingEdges(false);
        // 不允许调整大小
        this.graph.setCellsResizable(false);
        // 不允许两个节点间多条线
        this.graph.setMultigraph(false);
        // 允许拆分连线
        this.graph.setSplitEnabled(true);
        // 启用引导线 (Guide) 帮助对齐
        mxGraphHandler.prototype.guidesEnabled = true;
        // 通过拖拽选择多个元素
        new mxRubberband(this.graph);
    };

    #validConnection() {
        this.graph.isValidConnection = function (source, target) {
            return true;
        };
    }

    #configureShortcuts() {
        this.graphContainer.tabIndex = 0;
        this.graphContainer.style.outline = 'none';

        this.undoManager = new mxUndoManager();
        const listener = (sender, evt) => {
            this.undoManager.undoableEditHappened(evt.getProperty('edit'));
        };
        this.graph.getModel().addListener(mxEvent.UNDO, listener);
        this.graph.getView().addListener(mxEvent.UNDO, listener);


        const keyHandler = new mxKeyHandler(this.graph);
        // Delete
        keyHandler.bindKey(46, () => {
            const cells = this.graph.getSelectionCells();
            cells.length > 0 && this.graph.removeCells(cells);
        });
        // Ctrl-Z (撤销)
        keyHandler.bindControlKey(90, () => {
            if (this.undoManager.canUndo()) this.undoManager.undo();
        });
        // Ctrl-Y (重做)
        keyHandler.bindControlKey(89, () => {
            if (this.undoManager.canRedo()) this.undoManager.redo();
        });
        // Ctrl-A (全选)
        keyHandler.bindControlKey(65, () => {
            this.graph.selectAll();
        });
        // Ctrl-C
        keyHandler.bindControlKey(67, () => {
            if (!this.graph.isSelectionEmpty()) {
                mxClipboard.copy(this.graph);
            }
        });
        // Ctrl-V
        keyHandler.bindControlKey(86, () => {
            mxClipboard.paste(this.graph);
        });
    }

    #configureNodeStyle() {
        const stylesheet = this.graph.getStylesheet();

        // 设置连线默认样式
        const edgeStyle = stylesheet.getDefaultEdgeStyle();
        edgeStyle[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector;
        edgeStyle[mxConstants.STYLE_ROUNDED] = true;
        edgeStyle[mxConstants.STYLE_STROKEWIDTH] = 2;
        edgeStyle[mxConstants.STYLE_BENDING_VARIABLE] = true;
        edgeStyle[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_CLASSIC;

        // 基础通用样式
        const baseStyle = {
            [mxConstants.STYLE_ROUNDED]: true,
            [mxConstants.STYLE_STROKEWIDTH]: 2,
            [mxConstants.STYLE_FONTSIZE]: 16,
            [mxConstants.STYLE_FONTSTYLE]: mxConstants.FONT_BOLD,
            [mxConstants.STYLE_ALIGN]: mxConstants.ALIGN_CENTER,
            [mxConstants.STYLE_VERTICAL_ALIGN]: mxConstants.ALIGN_MIDDLE,
            [mxConstants.STYLE_PERIMETER_SPACING]: 4
        };

        // Start 节点样式
        {
            const style = mxUtils.clone(baseStyle);
            style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_ELLIPSE;
            style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
            style[mxConstants.STYLE_FILLCOLOR] = '#d4edda';
            style[mxConstants.STYLE_STROKECOLOR] = '#28a745';
            style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
            stylesheet.putCellStyle(JobFlow.NODE_START, style);
            this.shapeProps[JobFlow.NODE_START] = {width: 70, height: 70, name: 'Start'};
        }

        // End 节点样式
        {
            const style = mxUtils.clone(baseStyle);
            style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_ELLIPSE;
            style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
            style[mxConstants.STYLE_FILLCOLOR] = '#f8d7da';
            style[mxConstants.STYLE_STROKECOLOR] = '#dc3545';
            style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
            stylesheet.putCellStyle(JobFlow.NODE_END, style);
            this.shapeProps[JobFlow.NODE_END] = {width: 70, height: 70, name: 'End'};
        }

        // Decision 节点样式
        {
            const style = mxUtils.clone(baseStyle);
            style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RHOMBUS;
            style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RhombusPerimeter;
            style[mxConstants.STYLE_FILLCOLOR] = '#E1D5E7';
            style[mxConstants.STYLE_STROKECOLOR] = '#9673A6';
            style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
            stylesheet.putCellStyle(JobFlow.NODE_DECISION, style);
            this.shapeProps[JobFlow.NODE_DECISION] = {width: 180, height: 90, name: 'Decision'};
        }

        // Step 节点样式
        {
            const style = mxUtils.clone(baseStyle);
            style[mxConstants.STYLE_FILLCOLOR] = '#cfe2ff';
            style[mxConstants.STYLE_STROKECOLOR] = '#0d6efd';
            style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
            stylesheet.putCellStyle(JobFlow.NODE_STEP, style);
            this.shapeProps[JobFlow.NODE_STEP] = {width: 140, height: 60, name: 'Step'};
        }
    }

    // 自定义右键菜单
    #configureContextMenu() {
    }

    addStartNode(x, y) {
        const type = JobFlow.NODE_START;
        const prop = this.shapeProps[type];
        this.#addNode(type, prop.name, x, y, prop.width, prop.height);
    };

    bindToolbarComponent(elementId, type) {
        const element = document.getElementById(elementId);
        const shapeProps = this.shapeProps[type];
        this.#makeDraggable(element, type, shapeProps);
        this.#onClick(element, type, shapeProps);
    }

    exportModel() {
        const encoder = new mxCodec();
        const node = encoder.encode(this.graph.getModel());
        return mxUtils.getXml(node);
    }

    importModel(xmlString) {
        const doc = mxUtils.parseXml(xmlString);
        const decoder = new mxCodec(doc);
        decoder.decode(doc.documentElement, this.graph.getModel());
    }

    #onClick(element, type, shapeProps) {
        element.onclick = () => {
            const width = this.graphContainer.clientWidth;
            const height = this.graphContainer.clientHeight;

            // 中心区域范围（占容器的30%）
            const centerAreaRatio = 0.3;
            const centerWidth = width * centerAreaRatio;
            const centerHeight = height * centerAreaRatio;

            const startX = (width - centerWidth) / 2;
            const startY = (height - centerHeight) / 2;

            const x = startX + Math.random() * centerWidth;
            const y = startY + Math.random() * centerHeight;
            this.#addNode(type, shapeProps.name, x, y, shapeProps.width, shapeProps.height);
            this.graphContainer.focus();
        }
    }

    #makeDraggable(element, type, shapeProps) {
        // 拖拽时的预览
        const dragPreview = document.createElement('div');
        dragPreview.style.border = '1px dashed #000';
        dragPreview.style.width = shapeProps.width + 'px';
        dragPreview.style.height = shapeProps.height + 'px';

        const xOffset = -(shapeProps.width / 2);
        const yOffset = -(shapeProps.height / 2);

        // 绑定拖拽释放后的动作
        const onDrop = (graph, evt, cell, x, y) => {
            this.#addNode(type, shapeProps.name, x + xOffset, y + yOffset, shapeProps.width, shapeProps.height);
        };

        mxUtils.makeDraggable(element, this.graph, onDrop, dragPreview, xOffset, yOffset);
    }

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