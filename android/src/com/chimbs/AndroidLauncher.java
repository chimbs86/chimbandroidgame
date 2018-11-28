package com.chimbs;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.chimbs.MyGdxGame;
import com.sh.jplatformer.JPlatformerGame;
import com.sh.jplatformer.resources.ConstantRoot;
import com.sh.jplatformer.resources.Resources;
import com.sh.jplatformer.ui.stages.MainMenuStage;
import com.sh.jplatformer.util.FileUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        JPlatformerGame platformerGame = JPlatformerGame.get();


        AssetManager assetManager = getAssets();
        try {

            assetManager.open("Adventurous.worldfile");


            File root = getFilesDir();

            File resources = new File(root, "resources");
            resources.mkdir();
            createFileSystem(assetManager,(resources), "");
            MainMenuStage.rootDir = new File(resources,"worlds");
            ConstantRoot.ROOT = "";
            ConstantRoot.LANGUAGE_ROOT = root.getAbsolutePath();



        } catch (IOException e) {
            e.printStackTrace();
        }

        initialize(platformerGame, config);
    }


    public static void writeBytesToFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            fos.flush();
            if (fos != null) {
                fos.close();
            }
        }
    }

    private void createFileSystem(AssetManager assetManager, File parent, String assetParent)  {
        try {
            for (String asset : assetManager.list(assetParent.replaceFirst("/",""))) {
                if (asset.contains(".")) {
                    File file = new File(parent, asset);
                    file.createNewFile();
                    Log.i("creating", "creating: " + file.getAbsolutePath());
                    if(assetParent.isEmpty())
                    writeBytesToFile(assetManager.open(asset), file);
                    else{
                        writeBytesToFile(assetManager.open(assetParent.substring(1) + "/" + asset),file);
                    }
                } else if (!asset.isEmpty() || !asset.equals("")) {
                    File file = new File(parent, asset);
                    file.mkdir();
                    Log.i("creating", "creating: " + file.getAbsolutePath());
                    createFileSystem(assetManager, file, assetParent + "/" + asset);
                }

            }
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }


}
