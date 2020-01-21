/**
 * [ExtrinsicCrop]
 * A crop that is able to store information specific to the crop,
 * but relies on IntrinsicCrop for shared data between all crops.
 * 2019-12-23
 * @version 0.3
 * @author Joseph Wang, Paula Yuan
 */

public class ExtrinsicCrop extends ExtrinsicGrowableCollectable {

  /**
   * [ExtrinsicCrop]
   * Constructor for an ExtrinsicCrop that takes in an IntrinsicCrop 
   * that it sets as the related IntrinsicCrop.
   * @param crop An IntrinsicCrop which is the IntrinsicCrop of this crop.
   */
  public ExtrinsicCrop(IntrinsicCrop crop) {
    super(crop);
  }

  /**
   * [ExtrinsicCrop]
   * Constructor for an ExtrinsicCrop that takes in a string and finds 
   * the IntrinsicCrop related to that string.
   * @param crop A string with the crop's name.
   */
  public ExtrinsicCrop(String crop) {
    super(crop);
  }
}