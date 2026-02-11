class JobFlow {

    constructor(graphContainer, propertyContainer) {
        if (!mxClient.isBrowserSupported()) {
            mxUtils.error('浏览器不支持 mxGraph!', 200, false);
            return;
        }

        this.graphContainer = document.getElementById(graphContainer);
        if (!this.graphContainer) {
            throw new Error(`Container with id "${graphContainer}" not found.`);
        }
        this.propertyContainer = document.getElementById(propertyContainer);
        if (!this.propertyContainer) {
            throw new Error(`Container with id "${propertyContainer}" not found.`);
        }

        this.graph = new mxGraph(this.graphContainer);
        this.#configureGraph();
        this.#configureShortcuts();
        this.#configureCellStyle();
        this.#configureContextMenu();
        this.#configureSelectionListener();
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
        // 图形文字显示
        this.graph.convertValueToString = function (cell) {
            if (cell.isVertex()) {
                return cell.value.data?.beanName ?? cell.value.label;
            }
            return mxGraph.prototype.convertValueToString.apply(this, arguments);
        };
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

    #configureSelectionListener() {
        this.graph.getSelectionModel().addListener(mxEvent.CHANGE, (sender, evt) => {
            const cell = this.graph.getSelectionCell();
            if (!cell) {
                this.propertyContainer.innerHTML = `<h6 class="fw-bold border-bottom pb-2 mb-3 small">节点属性</h6>`;
            } else if (cell.isVertex()) {
                const value = cell.value;
                let html = `<h6 class="fw-bold border-bottom pb-2 mb-3 small">节点属性 - ${value.label}</h6>`;
                if (value.data) {
                    Object.keys(value.data).forEach(key => {
                        let propertyHtml = `
                            <div class="mb-2">
                                <label class="form-label small text-muted">${key}</label>
                                <input type="text" name="${key}" class="form-control form-control-sm" value="${value.data[key] || ''}">
                            </div>`;
                        html += propertyHtml;
                    });
                }
                this.propertyContainer.innerHTML = html;
            } else if (cell.isEdge()) {
                this.propertyContainer.innerHTML = `
                   <h6 class="fw-bold border-bottom pb-2 mb-3 small">节点属性 - 连线</h6>
                   <div class="mb-2">
                       <label class="form-label small text-muted">属性</label>
                       <input type="text" class="form-control form-control-sm" value="${cell.value || ''}">
                   </div>`;
            }
        });
    }

    #configureCellStyle() {
        const stylesheet = this.graph.getStylesheet();

        // 设置连线默认样式
        const edgeStyle = stylesheet.getDefaultEdgeStyle();
        edgeStyle[mxConstants.STYLE_EDGE] = mxEdgeStyle.ElbowConnector;
        edgeStyle[mxConstants.STYLE_ROUNDED] = true;
        edgeStyle[mxConstants.STYLE_STROKEWIDTH] = 2;
        edgeStyle[mxConstants.STYLE_BENDING_VARIABLE] = true;
        edgeStyle[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_CLASSIC;

        Object.keys(NodeFactory.nodes).forEach(key => {
            const node = NodeFactory.nodes[key];
            stylesheet.putCellStyle(node.type, node.getStyleConfig());
        });
    }

    // 自定义右键菜单
    #configureContextMenu() {
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

    addStartNode(x, y) {
        const baseNode = NodeFactory.createNode(BaseNode.NODE_START);
        this.#addNode(baseNode, x, y);
    };

    bindToolbarComponent(elementId, type) {
        const element = document.getElementById(elementId);
        this.#makeDraggable(element, type);
        this.#onClick(element, type);
    }

    #onClick(element, type) {
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
            const baseNode = NodeFactory.createNode(type);
            this.#addNode(baseNode, x, y);
            this.graphContainer.focus();
        }
    }

    #makeDraggable(element, type) {
        const baseNode = NodeFactory.createNode(type);
        // 拖拽时的预览
        const dragPreview = document.createElement('div');
        dragPreview.style.border = '1px dashed #000';
        dragPreview.style.width = baseNode.width + 'px';
        dragPreview.style.height = baseNode.height + 'px';

        const xOffset = -(baseNode.width / 2);
        const yOffset = -(baseNode.height / 2);

        // 绑定拖拽释放后的动作
        const onDrop = (graph, evt, cell, x, y) => {
            this.#addNode(baseNode, x + xOffset, y + yOffset);
        };

        mxUtils.makeDraggable(element, this.graph, onDrop, dragPreview, xOffset, yOffset);
    }

    #addNode(baseNode, x, y) {
        const parent = this.graph.getDefaultParent();
        this.graph.getModel().beginUpdate();
        try {
            const vertex = this.graph.insertVertex(parent, null, baseNode, x, y, baseNode.width, baseNode.height, baseNode.type);
            this.graph.setSelectionCell(vertex);
            return vertex;
        } finally {
            this.graph.getModel().endUpdate();
        }
    }

}