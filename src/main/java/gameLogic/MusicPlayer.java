package gameLogic;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * @author David J. Barnes und Michael Kölling
 * @version 31.07.2011
 */

/**
 * MusicPlayer plays the given music from folder audio
 */
public class MusicPlayer
{

    private AdvancedPlayer player;

    /**
     * creates a new MusicPlayer
     */
    public MusicPlayer()
    {
        player = null;
    }

    /**
     * Plays the music for a short time
     *
     * @param musicName Music to be played
     */
    public void shortPlay(String musicName)
    {
        try {
            playerPreparer(musicName);
            player.play(5000);
        }
        catch(JavaLayerException e) {
            printError(musicName);
        }
    }

    /**
     * Starts the music
     *
     * @param musicName Music to be played
     */
    public void startMusic(final String musicName)
    {
        try {
            playerPreparer(musicName);
            Thread playerThread = new Thread() {
                public void run()
                {
                    try {
                        player.play(5000);
                    }
                    catch(JavaLayerException e) {
                        printError(musicName);
                    }
                }
            };
            playerThread.start();
        }
        catch (Exception ex) {
            printError(musicName);
        }
    }

    /**
     * Stops the music
     */
    public void stop()
    {
        killPlayer();
    }

    /**
     * Prepares the music player with to music to be played
     *
     * @param musicName Music to be played
     */
    private void playerPreparer(String musicName)
    {
        try {
            InputStream is = giveInStream(musicName);
            player = new AdvancedPlayer(is, createAudioPlayer());
        }
        catch (IOException e) {
            printError(musicName);
            killPlayer();
        }
        catch(JavaLayerException e) {
            printError(musicName);
            killPlayer();
        }
    }

    /**
     * Inputstream with its given music
     *
     * @param musicName Music to be played
     * @return FactoryRegistry
     * @throws IOException
     */
    private InputStream giveInStream(String musicName)
            throws IOException
    {
        return new BufferedInputStream(
                new FileInputStream(musicName));
    }
    private AudioDevice createAudioPlayer()
            throws JavaLayerException
    {
        return FactoryRegistry.systemRegistry().createAudioDevice();
    }

    /**
     * Kills the music player
     */
    private void killPlayer()
    {
        synchronized(this) {
            if(player != null) {
                player.stop();
                player = null;
            }
        }
    }


    /**
     * Print an error message if something wrong happens
     * @param musicName Name of music to be played
     */
    private void printError(String musicName)
    {
        System.out.println("There was a problem with playing: " + musicName);
    }

}
