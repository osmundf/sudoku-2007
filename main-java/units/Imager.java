
package units;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
 * @author ZoftWhere
 * @version 08 Jul. 2007
 * 
 * @info The goal of this class is to read predefined images for the sudoku
 * program and split them into manageable images that will be loaded by the main
 * program.
 */

public class Imager {
	
	
	/**
	 * @param args
	 */
	public static void main (String [ ] args) {
		int width = 40 ;
		int height = 40 ;
		
		BufferedImage bi = new BufferedImage (width * 10, height, BufferedImage.TYPE_INT_ARGB) ;
		BufferedImage si = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB) ;
		
		
		// Note that the "zero" image for the user is black ...
		
		try {
			bi = ImageIO.read (new File ("sudoku/images/_u.png")) ;
		} catch (IOException e) {
			e.printStackTrace ( ) ;
		}
		
		for (int z = 0 ; z <= 9 ; z ++ ) {
			for (int x = 0 ; x < width ; x ++ )
				for (int y = 0 ; y < height ; y ++ )
					si.setRGB (x, y, bi.getRGB (x + z * width, y)) ;
			
			try {
				ImageIO.write (si, "png", new File ("sudoku/images/u" + z + ".png")) ;
			} catch (IOException e) {
				e.printStackTrace ( ) ;
			}
		}
		
		
		// Note that the "zero" image for the puzzle is asterixed ...
		
		try {
			bi = ImageIO.read (new File ("sudoku/images/_p.png")) ;
		} catch (IOException e) {
			e.printStackTrace ( ) ;
		}
		
		for (int z = 0 ; z <= 9 ; z ++ ) {
			for (int x = 0 ; x < width ; x ++ )
				for (int y = 0 ; y < height ; y ++ )
					si.setRGB (x, y, bi.getRGB (x + z * width, y)) ;
			
			try {
				ImageIO.write (si, "png", new File ("sudoku/images/p" + z + ".png")) ;
			} catch (IOException e) {
				e.printStackTrace ( ) ;
			}
		}
		
	}
	
}
