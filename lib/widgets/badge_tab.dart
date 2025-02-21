import 'package:badges/badges.dart' as badges;
import 'package:flutter/material.dart';

class BadgeTab extends StatelessWidget {
  final String text;
  final IconData icon;
  final int value;

  const BadgeTab({
    required this.text,
    required this.icon,
    this.value = 0,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final Widget icon;

    if (value > 0) {
      icon = _badgeIcon(theme);
    } else {
      icon = Icon(this.icon);
    }

    return Tab(
      icon: icon,
      text: text,
    );
  }

  Widget _badgeIcon(ThemeData theme) {
    return badges.Badge(
      badgeContent: Text(
        '$value',
        style: theme.textTheme.bodySmall?.copyWith(
          color: theme.colorScheme.onPrimary,
        ),
      ),
      badgeStyle: badges.BadgeStyle(
        badgeColor: theme.colorScheme.primary,
      ),
      child: Icon(icon),
    );
  }
}
