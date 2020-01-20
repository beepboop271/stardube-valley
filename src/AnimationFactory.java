import java.util.HashMap;

public class AnimationFactory { //TODO: JAVADOCS
  private static boolean isInitialized = false;
  private static HashMap<String, Animation> animationPool;

  private AnimationFactory() {
    // do not allow anyone to create an object of this class
  }

  public static void initializeComponents() {
    if (AnimationFactory.isInitialized) {
      return;
    }
    AnimationFactory.isInitialized = true;

    AnimationFactory.animationPool = new HashMap<String, Animation>();

  }

  public static Animation getAnimation(String animation) {
    if (!AnimationFactory.isInitialized) {
      throw new RuntimeException("AnimationFactory not initialized");
    }
    // shallow copy to share things like images
    return (Animation)(AnimationFactory.animationPool.get(animation).clone());
  }
}