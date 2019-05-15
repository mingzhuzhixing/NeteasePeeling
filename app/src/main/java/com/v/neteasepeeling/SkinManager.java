package com.v.neteasepeeling;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class SkinManager {

    private Context context;

    //皮肤插件的app资源对象
    private Resources resources;

    //外置皮肤插件的包名
    private String skinPackName;

    private static final SkinManager ourInstance = new SkinManager();

    static SkinManager getInstance() {
        return ourInstance;
    }

    private SkinManager() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 获取皮肤产经的资源对象
     */
    public void loadSkinApk(String path) {
        //获取到包管理器
        PackageManager packageManager = context.getPackageManager();

        //获取到皮肤插件的包名
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, packageManager.GET_ACTIVITIES);

        //获取插件apk包名
        skinPackName = packageArchiveInfo.packageName;

        try {
            AssetManager assetManager = AssetManager.class.newInstance();

            //获取到AssetManager对象中的addAssetPath方法
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);

            //执行这个addAssetPath方法
            addAssetPath.invoke(assetManager, path);

            //获取到外置apk的resource对象  选择计算字眼是要烤炉的当前显示度量  选择计算资源是要考路的当前所需需要的设备配置
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从外置插件中获取 资源对象中获取到颜色的资源
     */
    public int getColor(int id) {
        if (resources == null) {
            return id;
        }

        //获取到资源id的类型（@color /@drawable）
        String resourceTypeName = context.getResources().getResourceTypeName(id);

        //获取到资源id的属性名（@color后的名字）
        String resourceEntryName = context.getResources().getResourceEntryName(id);

        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, skinPackName);
        if (identifier == 0) {
            return id;
        }

        return resources.getColor(identifier);
    }

    /**
     * 从外置apk中拿到drawable的资源id
     */
    public Drawable getDrawable(int id) {
        if (resources == null) {
            return ContextCompat.getDrawable(context, id);
        }

        //获取到资源id的类型（@color /@drawable）
        String resourceTypeName = context.getResources().getResourceTypeName(id);

        //获取到资源id的属性名（@color后的名字）
        String resourceEntryName = context.getResources().getResourceEntryName(id);

        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, skinPackName);
        if (identifier == 0) {
            return ContextCompat.getDrawable(context, id);
        }

        return resources.getDrawable(identifier);
    }
}
