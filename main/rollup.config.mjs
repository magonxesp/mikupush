import typescript from '@rollup/plugin-typescript'
import copy from 'rollup-plugin-copy'

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
        typescript(),
        copy({
            targets: [
                { src: 'src/assets', dest: 'dist' },
            ],
        }),
    ],
};