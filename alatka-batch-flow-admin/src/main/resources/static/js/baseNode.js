class BaseData {
    constructor(beanName) {
        this.beanName = beanName;
    }
}

class BaseNode {

    static NODE_START = 'START';
    static NODE_END = 'END';
    static NODE_DECISION = 'DECISION';
    static NODE_STEP = 'STEP';

    constructor(label, width, height, type, data) {
        this.label = label;
        this.width = width;
        this.height = height;
        this.type = type;
        this.data = data;
    }

    getStyleConfig() {
        return {
            [mxConstants.STYLE_ROUNDED]: true,
            [mxConstants.STYLE_STROKEWIDTH]: 2,
            [mxConstants.STYLE_FONTSIZE]: 16,
            [mxConstants.STYLE_FONTSTYLE]: mxConstants.FONT_BOLD,
            [mxConstants.STYLE_ALIGN]: mxConstants.ALIGN_CENTER,
            [mxConstants.STYLE_VERTICAL_ALIGN]: mxConstants.ALIGN_MIDDLE,
            [mxConstants.STYLE_PERIMETER_SPACING]: 4
        };
    }

    isValidConnection(graph, source, target) {
        return source !== target;
    }

    isCellConnectable(graph, cell) {
        return true;
    }
}

class StartNode extends BaseNode {
    constructor() {
        super('Start', 70, 70, BaseNode.NODE_START);
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_ELLIPSE;
        style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
        style[mxConstants.STYLE_FILLCOLOR] = '#d4edda';
        style[mxConstants.STYLE_STROKECOLOR] = '#28a745';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isValidConnection(graph, source, target) {
        const flag = super.isValidConnection(source, target);
        return flag && target.value?.type !== BaseNode.NODE_START;
    }

    isCellConnectable(graph, cell) {
        return graph.getModel().getOutgoingEdges(cell).length < 1;
    }
}

class EndNode extends BaseNode {
    constructor() {
        super('End', 70, 70, BaseNode.NODE_END);
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_ELLIPSE;
        style[mxConstants.STYLE_PERIMETER] = mxPerimeter.EllipsePerimeter;
        style[mxConstants.STYLE_FILLCOLOR] = '#f8d7da';
        style[mxConstants.STYLE_STROKECOLOR] = '#dc3545';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isValidConnection(graph, source, target) {
        return super.isValidConnection(source, target);
    }

    isCellConnectable(graph, cell) {
        return false;
    }
}

class DecisionNode extends BaseNode {
    constructor() {
        super('Decision', 180, 90, BaseNode.NODE_DECISION, new BaseData());
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RHOMBUS;
        style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RhombusPerimeter;
        style[mxConstants.STYLE_FILLCOLOR] = '#E1D5E7';
        style[mxConstants.STYLE_STROKECOLOR] = '#9673A6';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isValidConnection(graph, source, target) {
        return super.isValidConnection(source, target);
    }

    isCellConnectable(graph, cell) {
        return super.isCellConnectable(cell, graph);
    }
}

class StepNode extends BaseNode {
    constructor() {
        super('Step', 140, 60, BaseNode.NODE_STEP, new BaseData());
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_FILLCOLOR] = '#cfe2ff';
        style[mxConstants.STYLE_STROKECOLOR] = '#0d6efd';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isValidConnection(graph, source, target) {
        return super.isValidConnection(source, target) && graph.getModel().getOutgoingEdges(target).length < 1;
    }

    isCellConnectable(graph, cell) {
        return graph.getModel().getOutgoingEdges(cell).length < 1;
    }
}

class NodeFactory {
    static nodes = {
        [BaseNode.NODE_START]: NodeFactory.createNode(BaseNode.NODE_START),
        [BaseNode.NODE_END]: NodeFactory.createNode(BaseNode.NODE_END),
        [BaseNode.NODE_DECISION]: NodeFactory.createNode(BaseNode.NODE_DECISION),
        [BaseNode.NODE_STEP]: NodeFactory.createNode(BaseNode.NODE_STEP),
    };

    static createNode(type) {
        switch (type) {
            case BaseNode.NODE_START:
                return new StartNode();
            case BaseNode.NODE_END:
                return new EndNode();
            case BaseNode.NODE_STEP:
                return new StepNode();
            case BaseNode.NODE_DECISION:
                return new DecisionNode();
            default:
                throw new Error("Unknown node type");
        }
    }

}
