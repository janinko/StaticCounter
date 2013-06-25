
package eu.janinko.andaria.staticcounter.staticcounter;

import eu.janinko.Andaria.ultimasdk.files.Arts;
import eu.janinko.Andaria.ultimasdk.files.Statics;
import eu.janinko.Andaria.ultimasdk.files.TileData;
import eu.janinko.Andaria.ultimasdk.files.arts.Art;
import eu.janinko.Andaria.ultimasdk.files.statics.Static;
import eu.janinko.Andaria.ultimasdk.files.tiledata.ItemData;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author janinko
 */
public class StaticCounter {
	private String uoPath;
	private Statics statics;
	private TileData tiledata;
	private Arts arts;


	public void setUOPath(String uoPath) throws IOException{
		if(Objects.equals(this.uoPath, uoPath)) return;
		this.uoPath = uoPath;
		init();
	}

	private void init() throws IOException {
		File staidx0 = new File(uoPath, "staidx0.mul");
		File statics0 = new File(uoPath, "statics0.mul");
		File artidx = new File(uoPath, "artidx.mul");
		File artmul = new File(uoPath, "art.mul");
		File tiledatamul = new File(uoPath, "tiledata.mul");

		statics = new Statics(staidx0, statics0);
		tiledata = new TileData(new FileInputStream(tiledatamul));
		arts = new Arts(artidx, artmul);
	}

	public Map<ItemData, Integer> getStatic(int startX, int startY, int stopX, int stopY) throws IOException{

		List<Static> sts = new ArrayList<Static>();
		for (int x = startX / 8; x < stopX / 8; x++){
			for(int y = startY / 8; y < stopY / 8; y++){
				sts.addAll(statics.getStaticsOnBlock(x, y));
			}
		}

		HashMap<ItemData, Integer> items = new HashMap<ItemData, Integer>();
		for(Static s : sts){
			if(s.getX() < startX || s.getY() < startY || s.getX() >= stopX || s.getY() >= stopY){
				continue;
			}
			ItemData item = tiledata.getItem(s.getId());
			Integer i = items.get(item);
			if(i == null){
				items.put(item, 1);
			}else{
				items.put(item, i + 1);
			}
		}

		return items;
	}

	public BufferedImage getImage(int id) throws IOException{
		Art a = arts.getArt(id);
		if(a == null) return null;
		return a.getImage();
	}
}
