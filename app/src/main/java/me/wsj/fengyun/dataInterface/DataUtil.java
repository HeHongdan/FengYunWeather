package me.wsj.fengyun.dataInterface;

public class DataUtil {
    private static DataInterface dataInterface;

    public static void setDataInterface(DataInterface inter) {
        dataInterface = inter;
    }

    public static void setCid(String cid) {
        if (dataInterface != null) {
            dataInterface.setCid(cid);
        }
    }

    public static void changeBack(String condCode){
        if (dataInterface!=null){
            dataInterface.changeBg(condCode);
        }
    }

}
