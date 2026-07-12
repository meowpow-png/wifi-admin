import type { DetailedHTMLProps, HTMLAttributes } from 'react'

type ScaleElementProps = DetailedHTMLProps<HTMLAttributes<HTMLElement>, HTMLElement>

declare module 'react' {
  namespace JSX {
    interface IntrinsicElements {
      'scale-button': ScaleElementProps & {
        type?: 'reset' | 'submit' | 'button'
        variant?: string
        size?: 'small' | 'large'
        disabled?: boolean
      }
      'scale-logo': ScaleElementProps & {
        variant?: 'magenta' | 'white'
        transparent?: boolean
        size?: number
        language?: string
        'logo-hide-title'?: boolean
      }
      'scale-icon-action-hide-password': ScaleElementProps & {
        size?: number
        'accessibility-title'?: string
      }
    }
  }
}
