package com.greypool.Eavesdrop;

import android.app.Fragment;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import com.parse.ParseCloud;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.*;

/**
 * Created with IntelliJ IDEA.
 * User: armen
 * Date: 11/6/13
 * Time: 3:21 AM
 */
public class RecordProcess extends Service {
	int SAMPLE_RATE = 16000;
	int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
			AudioFormat.ENCODING_PCM_16BIT);
	AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
			AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
	boolean UPLOAD_COMPLETE = false;
	String FILENAME;
	String state;
	ParseFile recording;
	ByteArrayOutputStream byteConverter;
	ByteBuffer audioBuffer;				// create buffer for full audio recording

	byte byteStream[] = new byte[BUFFER_SIZE*10];


	//TODO: wait for _start_recording message from parse, use ParseCloud object


	public IBinder onBind(Intent intent){
	return null;
	}

	public void record(){
		try {
			byteStream = audioBuffer.array();				//convert audio buffer bytebuffer to byte array
			recorder.startRecording();
			recorder.read(audioBuffer, BUFFER_SIZE);
		} catch (Exception e){
			System.out.println("Danger record ROBINSON!" + e);
		}
	}

	public void stop(){
		try{
			recorder.stop();
			recording = new ParseFile(byteStream);
			while(!UPLOAD_COMPLETE){
				recording.save();
				UPLOAD_COMPLETE = true;
				//upload file to parse database
				//set UPLOAD_COMPLETE TO TRUE
			}
		} catch (Exception e){
			System.out.println("Danger upload robinson" + e);
		}
	}
}
