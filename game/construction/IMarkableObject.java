package game.construction;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public interface IMarkableObject {

    void markObject();

    void unmarkObject();

    void target();

    default void cancelTarget() {
        return;
    }

    Placement getPlacement();
}
