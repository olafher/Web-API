package valandur.webapi.serialize.view.data;

import org.spongepowered.api.data.manipulator.mutable.entity.DamageableData;
import org.spongepowered.api.entity.EntitySnapshot;
import valandur.webapi.api.serialize.BaseView;

public class DamageableDataView extends BaseView<DamageableData> {

    public EntitySnapshot lastAttacker;
    public Double lastDamage;


    public DamageableDataView(DamageableData value) {
        super(value);

        this.lastAttacker = value.lastAttacker().get().orElse(null);
        this.lastDamage = value.lastDamage().get().orElse(null);
    }
}
