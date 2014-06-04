import java.awt.image.BufferedImage;

/**
 * information to support tile animation
 * @TODO consider adding a tile index size to support animations larger than the default 4 tiles.
 * @author test
 *
 */
public class AnimatedTile 
{
	private int m_indexOffset = 0;
	private int m_startIndex = 0;
	
	public int getM_indexOffset() {
		return m_indexOffset;
	}
	public void setM_indexOffset(int mIndexOffset) {
		m_indexOffset = mIndexOffset;
	}
	public int getM_startIndex() {
		return m_startIndex;
	}
	public void setM_startIndex(int mStartIndex) {
		m_startIndex = mStartIndex;
	}
	
	
	
}
