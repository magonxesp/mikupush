import * as React from 'react'

declare module 'react' {
    namespace JSX {
        interface IntrinsicElements {
            // Material Web Components type as normal html elements
            [elemName: `md-${string}`]: React.DetailedHTMLProps<React.HTMLAttributes<HTMLElement>, HTMLElement>
            'md-circular-progress': React.DetailedHTMLProps<React.HTMLAttributes<HTMLElement>, HTMLElement> & { value: number }
        }
    }
}
