package me.blackness.black.page;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.blackness.black.Page;
import me.blackness.black.event.ElementClickEvent;

/*
       .                                                    .
    .$"                                    $o.      $o.  _o"
   .o$$o.    .o$o.    .o$o.    .o$o.   .o$$$$$  .o$$$$$ $$P  `4$$$$P'   .o$o.
  .$$| $$$  $$' $$$  $$' $$$  $$' $$$ $$$| $$$ $$$| $$$ ($o  $$$: $$$  $$' $$$
  """  """ """  """ """  """ """  """ """  """ """  """  "   """  """ """  """
.oOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOo.
  ooo_ ooo ooo. ... ooo. ... ooo.  .. `4ooo.  .`4ooo.   ooo. ooo. ooo ooo.  ..
  $$$"$$$$ $$$| ... $$$| ... $$$$$$ ..    "$$o     "$$o $$$| $$$| $$$ $$$|   .
  $$$| $$$ $$$|     $$$|     $$$|     $$$: $$$ $$$: $$$ $$$| $$$| $$$ $$$|
  $$$| $$$ $$$| $o. $$$| $o. $$$| $o. $$$| $$$ $$$| $$$ $$$| $$$| $$$ $$$| $.
  $$$| $$$ $$$| $$$ $$$| $$$ $$$| $$$ $$$| $$$ $$$| $$$ $$$| $$$| $$$ $$$| $o.
  $$$| $$$ $$$| $$$ $$$| $$$ $$$| $$$ $$$| $$$ $$$| $$$ $$$| $$$| $$$ $$$| $$$
  $$$| $$$  $$. $$$  $$. $$$  $$. $$$ $$$| $$$ $$$| $$$ $$$| $$$| $$$  $$. $$$
  $$$: $P'  `4$$$Ü'__`4$$$Ü'  `4$$$Ü' $$$$$P'  $$$$$P'  $$$| $$$: $P' __`4$$$Ü'
 _ _______/∖______/  ∖______/∖______________/|________ "$P' _______/  ∖_____ _
                                                        i"  personinblack
                                                        |
 */
public class LiveTitlePage implements Page {
    private final Page basePage;
    private final Plugin plugin;
    private final int period;
    private final String[] frames;

    public LiveTitlePage(Page basePage, Plugin plugin, int period, String... frames) {
        this.basePage = basePage;
        this.plugin = plugin;
        this.period = period;
        this.frames = frames;
    }

    @Override
	public void showTo(Player player) {
        new BukkitRunnable(){
            private Page currentPage;
            private int index;

            @Override
            public void run() {
                if (currentPage != null) {
                    final InventoryHolder activePage = player.getOpenInventory()
                        .getTopInventory().getHolder();

                    if (!(activePage instanceof Page) || !currentPage.equals((Page) activePage)) {
                        cancel();
                        return;
                    }
                }

                final String nextTitle = frames[index++];

                (currentPage = basePage.clone(nextTitle)).showTo(player);

                if (index == frames.length) {
                    index = 0;
                }
            }
        }.runTaskTimer(plugin, 0, period);
    }

    @Override
	public void handleClose(InventoryCloseEvent event) {
        basePage.handleClose(event);
    }

    @Override
	public void update(Object argument) {
        basePage.update(argument);
	}

	@Override
	public Inventory getInventory() {
		return basePage.getInventory();
	}

	@Override
	public void accept(ElementClickEvent event) {
        basePage.accept(event);
    }

    @Override
    public Page clone(String title) {
        return new LiveTitlePage(basePage, plugin, period, frames);
    }
}
