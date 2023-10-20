package me.kyroclient.mixins.skyhanni;

import at.hannibal2.skyhanni.events.FarmingContestEvent;
import at.hannibal2.skyhanni.features.garden.contest.FarmingContestPhase;
import at.hannibal2.skyhanni.features.garden.contest.JacobContestStatsSummary;
import me.kyroclient.modules.garden.AntiJacob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JacobContestStatsSummary.class)
public class MixinJacobStatSummary {
    @Inject(method = "onFarmingContestEvent", at = @At("HEAD"), remap = false, cancellable = true)
    public void onContest(FarmingContestEvent event, CallbackInfo ci)
    {
        if (event.getPhase() == FarmingContestPhase.START)
        {
            AntiJacob.instance.disable();
        }
        else if (event.getPhase() == FarmingContestPhase.STOP)
        {
            AntiJacob.instance.reenable();
        }
    }
}
