public abstract class ExtrinsicTileComponent extends TileComponent {
  private final IntrinsicTileComponent intrinsicSelf;

  public ExtrinsicTileComponent(IntrinsicTileComponent intrinsicSelf) {
    super();
    this.intrinsicSelf = intrinsicSelf;
  }

  public ExtrinsicTileComponent(String intrinsicSelf) {
    this.intrinsicSelf = IntrinsicTileComponentFactory.getComponent(intrinsicSelf);
  }

  public IntrinsicTileComponent getIntrinsicSelf() {
    return this.intrinsicSelf;
  }
}