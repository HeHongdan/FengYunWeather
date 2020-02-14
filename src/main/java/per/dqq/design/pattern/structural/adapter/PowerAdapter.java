package per.dqq.design.pattern.structural.adapter;

/**
 *
 */
public class PowerAdapter implements DC5V {
    private AC220V ac220V;

    public PowerAdapter(AC220V ac220V) {
        this.ac220V = ac220V;
    }

    @Override
    public int outputDC5v() {
        int intput = ac220V.inputAC220V();
        // 变压器将220v交流电转换成5v直流电
        int output = intput / 44;
        return output;
    }
}
