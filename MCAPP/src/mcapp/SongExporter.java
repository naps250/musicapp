package mcapp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.os.Environment;

/**
 * Exports songs.
 * @author Sean
 *
 */
public class SongExporter
{
	/**
	 * Exports the Song Object to a file for storage.
	 * @param fileName Name of File
	 * @param song Current Song instance
	 */
	public static void songExport(String fileName, Song song)
	{
		//Creates File Path for Song
		String _filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		_filePath += "/" + fileName + ".txt";

		//Current Instance of Song
		Score tScore = song.getScore(0);

		//Export Stream
		byte[] stream = new byte[(12*3*4)*Integer.SIZE];
		byte[] name = song.getName().getBytes();
		byte[] description = song.getDescription().getBytes();
		int count = 0;

		for (int i=0; i<12; i++) 
		{
		    Beat beat = tScore.getBeat(i);
		    Note[] notes = beat.getNotes();

		    int j = 0;

		    //Converts Note Information into Bytes
		    for(Note n : notes)
		    {
		    	byte instrumentID = (byte) (n.getInstrument() & 0xff);
	            byte pitch = (byte) ((n.getPitch() & 0xff00 ) >> 8);
	            byte beatPos = (byte) ((byte) (i & 0xff0000 ) >> 8);
	            byte notePos = (byte) ((byte) (j & 0xff0001 ) >> 8);

				stream[count] = instrumentID;    
	            stream[count + 1] = pitch;
	            stream[count + 2] = beatPos;
	            stream[count + 3] = notePos;

	            count = count + 4;
	            j++;
		    }
		    j = 0;
		 }

		try
		{
			FileOutputStream fileOut = new FileOutputStream(_filePath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			//Use this to test the system output to see if data is passing out.
			//System.out.println(name);
			//System.out.println(description);
			//System.out.println(stream);

			//Outputs Byte Information
			out.write('#');
			out.write(name);
			out.write('#');
			out.write(description);
			out.write('#');
			out.write(stream);
			out.write('#');
			out.close();

			//Delete Instances
			name = null;
			description = null;
			stream = null;

		    fileOut.close();
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}
	}
}