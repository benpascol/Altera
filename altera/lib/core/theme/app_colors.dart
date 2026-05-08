import 'package:flutter/material.dart';

class AppColors {
  // Base Colors from Stitch "Altera Obsidian"
  static const Color obsidian = Color(0xFF0E1511);
  static const Color deepSpace = Color(0xFF0A0F0D);
  static const Color emerald = Color(0xFF10B981);
  static const Color tealCustom = Color(0xFF14B8A6);
  static const Color glassBase = Color(0x1AFFFFFF); // White with 10% opacity
  static const Color glassText = Color(0xE6FFFFFF); // White with 90% opacity
  
  // Accents
  static const Color accentIndigo = Color(0xFF6366F1);
  static const Color errorRed = Color(0xFFEF4444);

  // Gradients
  static const LinearGradient obsidianGradient = LinearGradient(
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
    colors: [obsidian, deepSpace],
  );
}
