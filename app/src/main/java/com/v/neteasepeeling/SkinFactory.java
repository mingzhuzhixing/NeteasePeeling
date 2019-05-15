package com.v.neteasepeeling;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SkinFactory implements LayoutInflater.Factory2 {
    //收集已经创建了的activity中的空间来进行换肤
    private List<SkinView> viewList =new ArrayList<>();

    private static final String[] prsfixList={
            "android.widget.","android.view.","android.webkit."
    };

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        Log.i("factory","---->"+name);
        //第一步：收集要换肤的控件
        View view=null;

        //判读这个view是否是自定义的view 还是系统的view
        if(name.contains(".")){
            //实例化view  onCreateView是下面的
            view= onCreateView(name,context,attrs);
        }else{
            for (String s:prsfixList){
                view= onCreateView(s+name,context,attrs);
                if(view!=null){
                    break;
                }
            }
        }

        if(view!=null){
            //如果控件部位空  我就去判断你这个控件是否需要换肤

            parseView(view,name,attrs);
        }


        return view;
    }

    /**
     * 判断你这个控件是否需要换肤  如果需要就收集到集合中
     */
    private void parseView(View view, String name, AttributeSet attrs) {
        List<SkinItem> itemList=new ArrayList<>();

        for (int x=0;x<attrs.getAttributeCount();x++ ){
            //获取到属性的名字
            String attributeName = attrs.getAttributeName(x);

            //获取属性的id
            String attributeValueId = attrs.getAttributeValue(x);

            //判断这个控件是不是需要换肤的控件
            if(attributeName.contains("background") || attributeName.contains("textColor") || attributeName.contains("src")){
                int id = Integer.parseInt(attributeValueId.substring(1));

                String resourceTypeName = view.getResources().getResourceTypeName(id);
                String resourceEntryName = view.getResources().getResourceEntryName(id);

                SkinItem skinItem=new SkinItem(attributeName,id,resourceEntryName,resourceTypeName);
                itemList.add(skinItem);
            }
        }

        if(itemList.size()>0){
            SkinView skinView=new SkinView(view,itemList);
            viewList.add(skinView);
            skinView.apply();
        }
    }

    /**
     * 给所有收集起来的view换肤
     */
    public void apply(){
        for (SkinView skinView:viewList){
            skinView.apply();
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view=null;

        try {
            Class aClass = context.getClassLoader().loadClass(name);

            Constructor<? extends View> constructor = aClass.getConstructor(new Class[]{Context.class, AttributeSet.class});

            view=constructor.newInstance(context,attrs);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 先封装空间的属性
     */
    class SkinItem{
        //属性的名字 background
        String name;

        //属性的资源id
        int resId;

        //属性的文件名
        String entryName;

        //属性资源的类型
        String typeName;

        public SkinItem(String name, int resId, String entryName, String typeName) {
            this.name = name;
            this.resId = resId;
            this.entryName = entryName;
            this.typeName = typeName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public String getEntryName() {
            return entryName;
        }

        public void setEntryName(String entryName) {
            this.entryName = entryName;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    /**
     * 把单个空间view精心封装
     */
    class SkinView{
        //
        private View view;

        //控件的所有属性的集合
        List<SkinItem> list;

        public SkinView(View view, List<SkinItem> list) {
            this.view = view;
            this.list = list;
        }

        /**
         * 给单个控件换肤
         */
        public void apply(){
            for (SkinItem skinItem:list){
                if(skinItem.getName().equals("background")){
                    if (skinItem.getTypeName().equals("color")){
                        view.setBackgroundColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    }else if (skinItem.getTypeName().equals("drawable") || skinItem.getTypeName().equals("mipmap")){
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                            view.setBackground(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }else{
                            view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }
                    }
                }else if(skinItem.getName().equals("src")){
                    if (skinItem.getTypeName().equals("drawable") || skinItem.getTypeName().equals("mipmap")){
                        if(view instanceof ImageView){
                            ((ImageView)view).setImageDrawable(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }
                    }
                }else if(skinItem.getName().equals("textColor")){
                    if(view instanceof TextView){
                        ((TextView)view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    }else if(view instanceof Button){
                        ((Button)view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    }

                }
            }
        }
    }
}
