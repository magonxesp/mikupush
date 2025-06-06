import { FusesPlugin } from '@electron-forge/plugin-fuses'
import type { ForgeConfig } from '@electron-forge/shared-types'
import { FuseV1Options, FuseVersion } from '@electron/fuses'

const devMode = process.env.BUILD_ENV === 'DEV'

const config: ForgeConfig = {
	packagerConfig: {
		asar: true,
		ignore: [
			/^\/src/,
			/^\/installer/,
			/^\/target/,
			/^\/tsconfig.*/,
			/^\/\.env/,
			/^\/\.md/,
			/^\/resources/,
			/^\/vite\.config\.ts/,
			/^\/eslint\.config\.mjs/,
			/^\/forge\.config\.ts/,
			/^\/index\.html/,
			/^\/\.gitignore/
		],
		overwrite: true,
		icon: 'resources/icon/icon',
		appBundleId: 'io.mikupush.client',
		executableName: 'mikupush',
		osxSign: devMode ? undefined : {},
		osxNotarize: devMode ? undefined : {
			appleId: process.env.APPLE_ID!,
			appleIdPassword: process.env.APPLE_PASSWORD!,
			teamId: process.env.APPLE_TEAM_ID!
		}
	},
	outDir: 'target',
	rebuildConfig: {
		force: true
	},
	makers: [
		{
			name: '@electron-forge/maker-squirrel',
			platforms: ['win32'],
			config: {
				iconUrl: 'https://mikupush.io/favicon.ico',
				setupIcon: 'resources/icon/icon.ico',
			},
		},
		{
			name: '@electron-forge/maker-zip',
			platforms: ['darwin', 'win32', 'linux'],
			config: {},
		},
		{
			name: '@electron-forge/maker-deb',
			platforms: ['linux'],
			config: {
				icon: 'resources/icon/icon.png',
			},
		},
		{
			name: '@electron-forge/maker-rpm',
			platforms: ['linux'],
			config: {
				icon: 'resources/icon/icon.png',
			},
		},
		{
			name: '@electron-forge/maker-dmg',
			platforms: ['darwin'],
			config: {
				icon: 'resources/icon/icon.icns',
				background: 'resources/dmg/background.png',
				format: 'ULFO'
			}
		}
	],
	plugins: [
		{
			name: '@electron-forge/plugin-auto-unpack-natives',
			config: {},
		},
		// Fuses are used to enable/disable various Electron functionality
		// at package time, before code signing the application
		new FusesPlugin({
			version: FuseVersion.V1,
			[FuseV1Options.RunAsNode]: false,
			[FuseV1Options.EnableCookieEncryption]: true,
			[FuseV1Options.EnableNodeOptionsEnvironmentVariable]: false,
			[FuseV1Options.EnableNodeCliInspectArguments]: false,
			[FuseV1Options.EnableEmbeddedAsarIntegrityValidation]: true,
			[FuseV1Options.OnlyLoadAppFromAsar]: true,
		}),
	],
}

export default config
