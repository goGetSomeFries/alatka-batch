class BaseNodeData {
    constructor(beanName) {
        this.beanName = beanName;
    }
}

class SplitNodeData {
    constructor(taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
}

class BaseNode {

    static NODE_START = 'START';
    static NODE_END = 'END';
    static NODE_FAIL = 'FAIL';
    static NODE_STOP = 'STOP';
    static NODE_DECISION = 'DECISION';
    static NODE_SPLIT = 'SPLIT';
    static NODE_JOIN = 'JOIN';
    static NODE_STEP = 'STEP';
    static NODE_FLOW = 'FLOW';

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

    isValidSourceConnection(graph, source, target) {
        return source !== target;
    }

    isValidTargetConnection(graph, source, target) {
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

    isValidTargetConnection(graph, source, target) {
        return false;
    }

    isCellConnectable(graph, cell) {
        return graph.getModel().getOutgoingEdges(cell).length === 0;
    }
}

class EndNode extends BaseNode {
    constructor() {
        super('End', 60, 60, BaseNode.NODE_END);
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
        style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
        style[mxConstants.STYLE_ARCSIZE] = 25;
        style[mxConstants.STYLE_FILLCOLOR] = '#F5F5F5';
        style[mxConstants.STYLE_STROKECOLOR] = '#666666';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isCellConnectable(graph, cell) {
        return false;
    }
}

class StoppedNode extends BaseNode {
    constructor() {
        super('Stop', 60, 60, BaseNode.NODE_STOP);
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
        style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
        style[mxConstants.STYLE_ARCSIZE] = 25;
        style[mxConstants.STYLE_FILLCOLOR] = '#fff3cd';
        style[mxConstants.STYLE_STROKECOLOR] = '#ffc107';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isCellConnectable(graph, cell) {
        return false;
    }
}

class FailedNode extends BaseNode {
    constructor() {
        super('Fail', 60, 60, BaseNode.NODE_FAIL);
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
        style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
        style[mxConstants.STYLE_ARCSIZE] = 25;
        style[mxConstants.STYLE_FILLCOLOR] = '#f8d7da';
        style[mxConstants.STYLE_STROKECOLOR] = '#dc3545';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isCellConnectable(graph, cell) {
        return false;
    }
}

class DecisionNode extends BaseNode {
    constructor() {
        super('Decision', 180, 90, BaseNode.NODE_DECISION, new BaseNodeData());
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RHOMBUS;
        style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RhombusPerimeter;
        style[mxConstants.STYLE_FILLCOLOR] = '#FFFFFF';
        style[mxConstants.STYLE_STROKECOLOR] = '#343A40';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isValidTargetConnection(graph, source, target) {
        return super.isValidTargetConnection(source, target) && graph.getModel().getIncomingEdges(target).length === 0;
    }

}

class SplitNode extends BaseNode {
    constructor() {
        super('Split', 60, 140, BaseNode.NODE_SPLIT, new SplitNodeData());
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_TRIANGLE;
        style[mxConstants.STYLE_DIRECTION] = mxConstants.DIRECTION_WEST;
        style[mxConstants.STYLE_PERIMETER] = mxPerimeter.TrianglePerimeter;
        style[mxConstants.STYLE_ARCSIZE] = 30;
        style[mxConstants.STYLE_FILLCOLOR] = '#ffe5d0';
        style[mxConstants.STYLE_STROKECOLOR] = '#fd7e14';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isValidSourceConnection(graph, source, target) {
        return super.isValidSourceConnection(graph, source, target) && target.value.type === BaseNode.NODE_FLOW;
    }

    isValidTargetConnection(graph, source, target) {
        return super.isValidTargetConnection(source, target) && graph.getModel().getIncomingEdges(target).length === 0;
    }

}

class JoinNode extends BaseNode {
    constructor() {
        super('Join', 60, 140, BaseNode.NODE_JOIN);
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_TRIANGLE;
        style[mxConstants.STYLE_DIRECTION] = mxConstants.DIRECTION_EAST;
        style[mxConstants.STYLE_PERIMETER] = mxPerimeter.TrianglePerimeter;
        style[mxConstants.STYLE_ARCSIZE] = 30;
        style[mxConstants.STYLE_FILLCOLOR] = '#ffe5d0';
        style[mxConstants.STYLE_STROKECOLOR] = '#fd7e14';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isValidTargetConnection(graph, source, target) {
        return super.isValidTargetConnection(source, target) && source.value.type === BaseNode.NODE_FLOW;
    }

    isCellConnectable(graph, cell) {
        return graph.getModel().getOutgoingEdges(cell).length === 0;
    }
}

class StepNode extends BaseNode {
    constructor() {
        super('Step', 140, 60, BaseNode.NODE_STEP, new BaseNodeData());
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_FILLCOLOR] = '#cfe2ff';
        style[mxConstants.STYLE_STROKECOLOR] = '#0d6efd';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isValidTargetConnection(graph, source, target) {
        return super.isValidTargetConnection(source, target) && graph.getModel().getIncomingEdges(target).length === 0;
    }

    isCellConnectable(graph, cell) {
        return graph.getModel().getOutgoingEdges(cell).length === 0;
    }
}

class FlowNode extends BaseNode {
    constructor() {
        super('Flow', 140, 60, BaseNode.NODE_FLOW, new BaseNodeData());
    }

    getStyleConfig() {
        const style = super.getStyleConfig();
        style[mxConstants.STYLE_DASHED] = true;
        style[mxConstants.STYLE_DASH_PATTERN] = '3 3';
        style[mxConstants.STYLE_FILLCOLOR] = '#E1D5E7';
        style[mxConstants.STYLE_STROKECOLOR] = '#9673A6';
        style[mxConstants.STYLE_FONTCOLOR] = style[mxConstants.STYLE_STROKECOLOR];
        return style;
    }

    isValidTargetConnection(graph, source, target) {
        return super.isValidTargetConnection(source, target) && graph.getModel().getIncomingEdges(target).length === 0;
    }

    isCellConnectable(graph, cell) {
        return graph.getModel().getOutgoingEdges(cell).length === 0;
    }
}

class NodeFactory {

    static CLASSES = [StartNode, EndNode, StoppedNode, FailedNode, DecisionNode, SplitNode, JoinNode, StepNode, FlowNode, BaseNodeData];

    static NODES = {
        [BaseNode.NODE_START]: NodeFactory.createNode(BaseNode.NODE_START),
        [BaseNode.NODE_END]: NodeFactory.createNode(BaseNode.NODE_END),
        [BaseNode.NODE_STOP]: NodeFactory.createNode(BaseNode.NODE_STOP),
        [BaseNode.NODE_FAIL]: NodeFactory.createNode(BaseNode.NODE_FAIL),
        [BaseNode.NODE_DECISION]: NodeFactory.createNode(BaseNode.NODE_DECISION),
        [BaseNode.NODE_SPLIT]: NodeFactory.createNode(BaseNode.NODE_SPLIT),
        [BaseNode.NODE_JOIN]: NodeFactory.createNode(BaseNode.NODE_JOIN),
        [BaseNode.NODE_STEP]: NodeFactory.createNode(BaseNode.NODE_STEP),
        [BaseNode.NODE_FLOW]: NodeFactory.createNode(BaseNode.NODE_FLOW),
    };

    static createNode(type) {
        switch (type) {
            case BaseNode.NODE_START:
                return new StartNode();
            case BaseNode.NODE_END:
                return new EndNode();
            case BaseNode.NODE_STOP:
                return new StoppedNode();
            case BaseNode.NODE_FAIL:
                return new FailedNode();
            case BaseNode.NODE_STEP:
                return new StepNode();
            case BaseNode.NODE_FLOW:
                return new FlowNode();
            case BaseNode.NODE_DECISION:
                return new DecisionNode();
            case BaseNode.NODE_SPLIT:
                return new SplitNode();
            case BaseNode.NODE_JOIN:
                return new JoinNode();
            default:
                throw new Error("Unknown node type");
        }
    }

}
