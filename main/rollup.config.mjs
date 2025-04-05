import typescript from '@rollup/plugin-typescript'
import copy from 'rollup-plugin-copy'
import externals from 'rollup-plugin-node-externals'

/**
 * @type {import('rollup').RollupOptions}
 */
export default {
	input: [
        'src/main.ts',
        'src/preload.ts'
    ],
	output: {
        dir: 'dist',
		format: 'cjs',
        sourcemap: true
	},
	plugins: [
        externals(),
        typescript(),
        copy({
            targets: [
                { src: 'src/assets', dest: 'dist' },
                { src: '../renderer/dist', dest: 'renderer' },
            ],
        }),
    ],
};