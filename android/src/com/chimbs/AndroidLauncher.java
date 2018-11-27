package com.chimbs;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.chimbs.MyGdxGame;
import com.sh.jplatformer.JPlatformerGame;
import com.sh.jplatformer.ui.stages.MainMenuStage;
import com.sh.jplatformer.util.FileUtils;

import java.io.File;
import java.io.FileDescriptor;
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

getResources();
            File resources = new File(getFilesDir(), "resources");
            resources.mkdir();
            File adventurous = new File(resources, "Adventurous.worldfile");
            adventurous.createNewFile();
            File river = new File(resources, "river.worldfile");
            river.createNewFile();
            for(String asset : getAssets().list("")){
                writeBytesToFile(assetManager.open(asset), new File(resources,asset));
            }

            writeBytesToFile(assetManager.open("Adventurous.worldfile"), adventurous);
            writeBytesToFile(assetManager.open("Adventurous.worldfile"), river);
            MainMenuStage.rootDir = resources;

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
            if (fos != null) {
                fos.close();
            }
        }
    }
}
