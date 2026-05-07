class AppConstants {
  // === FINANCE CATEGORIES ===
  static const List<String> financeCategories = [
    "Food",
    "Transportation",
    "Entertainment",
    "Shopping",
    "Health",
    "Education",
    "Utilities",
    "Gifts",
    "Invoice",
    "Others"
  ];

  // === TASK CATEGORIES ===
  static const List<String> taskCategories = [
    "Work",
    "Personal",
    "Study",
    "Shopping",
    "Health",
    "Education",
    "Others"
  ];

  // === GAMIFICATION ===
  static const int maxXpPerLevel = 100;
  static const int xpBonusBase = 5;
  static const int xpMultiplierPerDifficulty = 10;

  // === FIRESTORE PATHS ===
  static const String usersCollection = "users";
  static const String tasksSubcollection = "tasks";
  static const String transactionsSubcollection = "transactions";

  // === UI ===
  static const Duration splashDelay = Duration(milliseconds: 2000);

  // === ML NORMALIZATION ===
  static const double maxExpense = 64997.0;
  static const double maxBalance = 14213582.0;
  static const double maxTaskCount = 20.0;
  static const double maxDuration = 8.0;
  static const double maxSleep = 24.0;
  static const double maxDailyExpense = 249647.0;

  // === LOCAL STORAGE KEYS ===
  static const String prefName = "AlteraPrefs";
  static const String keyIsDarkTheme = "is_dark_theme";
  static const String keyUserToken = "user_token";
  static const String keyUserName = "user_name";
  static const String keyIsLoggedIn = "is_logged_in";

  // === APP INFO ===
  static const String appName = "ALTERA";
  static const String minPasswordLength = "6";
}
