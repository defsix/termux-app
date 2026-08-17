# defsix Termux changelog

## v0.118.3-defsix.2

### Changed
- The generic Android file-view handler is enabled by default, matching upstream Termux behaviour.
- Users can opt out with the "Show Termux in \"Open with\" menus" setting.

### Fixed
- The file-viewer toggle now persists the user's explicit choice across Settings reopen, app restarts, force-stops, and device restarts.
- The saved toggle state is reapplied to the Android file-viewer component when Termux starts, preventing an explicit OFF choice from reverting to the default ON state.

## v0.118.3-defsix.1

### Added
- Runtime setting to show or hide Termux in Android "Open with" menus.
- GitHub Actions workflow that builds downloadable debug APK artifacts.

### Changed
- The generic file-view handler is disabled by default in this fork and can be enabled from Termux Settings.
- The setting is labelled "Show Termux in \"Open with\" menus" with a clear description of its effect.

### Fixed
- Updated the Gradle Wrapper validation workflow to use a valid Gradle Actions release.

### Preserved
- Sharing files to Termux through Android's Share menu remains enabled.
