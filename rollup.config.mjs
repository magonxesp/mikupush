import copy from 'rollup-plugin-copy'
import del from 'rollup-plugin-delete'

/**
 * @type {import('rollup').RollupOptions}
 */
export default {
    input: 'entry.js',
    output: {
        dir: 'build',
    },
	plugins: [
        del({ targets: 'build/*' }),
        copy({
            targets: [
                { src: 'main/dist', dest: 'build' },
                { src: 'main/renderer', dest: 'build' },
                { src: 'main/package.json', dest: 'build' },
            ],
        }),
    ],
};