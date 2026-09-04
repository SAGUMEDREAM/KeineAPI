package cc.thonly.keine.api.loot;

public enum LootTableSource {
   VANILLA(true),
   MOD(true),
   DATA_PACK(false),
   REPLACED(false);

   private final boolean builtin;

   private LootTableSource(boolean builtin) {
      this.builtin = builtin;
   }

   public boolean isBuiltin() {
      return this.builtin;
   }
}
