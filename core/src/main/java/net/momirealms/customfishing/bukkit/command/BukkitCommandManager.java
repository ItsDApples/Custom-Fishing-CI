/*
 *  Copyright (C) <2024> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customfishing.bukkit.command;

import net.kyori.adventure.util.Index;
import net.momirealms.customfishing.api.BukkitCustomFishingPlugin;
import net.momirealms.customfishing.bukkit.command.feature.*;
import net.momirealms.customfishing.common.command.AbstractCommandManager;
import net.momirealms.customfishing.common.command.CommandFeature;
import net.momirealms.customfishing.common.sender.Sender;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import org.incendo.cloud.setting.ManagerSetting;

import java.util.List;

public class BukkitCommandManager extends AbstractCommandManager<CommandSender> {

    private final List<CommandFeature<CommandSender>> FEATURES = List.of(
            new ReloadCommand(this),
            new SellFishCommand(this),
            new GetItemCommand(this),
            new GiveItemCommand(this),
            new GiveItemByUUIDCommand(this),
            new ImportItemCommand(this),
            new EndCompetitionCommand(this),
            new StopCompetitionCommand(this),
            new StartCompetitionCommand(this),
            new OpenMarketCommand(this),
            new OpenBagCommand(this),
            new FishingBagCommand(this),
            new EditOnlineBagCommand(this),
            new EditOfflineBagCommand(this),
            new UnlockDataCommand(this),
            new ImportDataCommand(this),
            new ExportDataCommand(this),
            new AddStatisticsCommand(this),
            new SetStatisticsCommand(this),
            new ResetStatisticsCommand(this),
            new QueryStatisticsCommand(this),
            new DebugLootCommand(this),
            new DebugBiomeCommand(this)
    );

    private final Index<String, CommandFeature<CommandSender>> INDEX = Index.create(CommandFeature::getFeatureID, FEATURES);

    public BukkitCommandManager(BukkitCustomFishingPlugin plugin) {
        super(plugin, new LegacyPaperCommandManager<>(
                plugin.getBootstrap(),
                ExecutionCoordinator.simpleCoordinator(),
                SenderMapper.identity()
        ));
        final LegacyPaperCommandManager<CommandSender> manager = (LegacyPaperCommandManager<CommandSender>) getCommandManager();
        manager.settings().set(ManagerSetting.ALLOW_UNSAFE_REGISTRATION, true);
        if (manager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            manager.registerBrigadier();
            manager.brigadierManager().setNativeNumberSuggestions(true);
        } else if (manager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            manager.registerAsynchronousCompletions();
        }
    }

    @Override
    protected Sender wrapSender(CommandSender sender) {
        return ((BukkitCustomFishingPlugin) plugin).getSenderFactory().wrap(sender);
    }

    @Override
    public Index<String, CommandFeature<CommandSender>> getFeatures() {
        return INDEX;
    }
}
