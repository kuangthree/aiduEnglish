package ecnu.ireader.function_module;

/**
 * Created by Shensheng on 2017/5/15.
 * 存储一些用户配置
 */

public class UserConfig {
    private static UserConfig sUserConfig = null;

    public static UserConfig getInstance(){
        if(sUserConfig == null){
            sUserConfig = new UserConfig();
        }
        return sUserConfig;
    }

    private String mLevel = Dictionary.LEVEL_4;
    private int mMode = PassageFilter.MODE_ANNO;

    public UserConfig setLevel(String level){
        mLevel = level;
        return this;
    }
    public UserConfig setLevel(int l){
        mLevel = (new String[]{Dictionary.LEVEL_ZK,Dictionary.LEVEL_GK,Dictionary.LEVEL_4,Dictionary.LEVEL_6,Dictionary.LEVEL_8})[l];
        return this;
    }
    public UserConfig setMode(int mode){
        mMode = mode;
        return this;
    }
    public int getMode(){
        return mMode;
    }
    public String getLevel(){
        return mLevel;
    }
    private UserConfig(){

    }
}
