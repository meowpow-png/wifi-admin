import type {ComponentPropsWithoutRef} from 'react'
import {useId, useState} from 'react'
import {Eye, EyeOff, Wifi} from 'lucide-react'

import styles from './LoginPage.module.css'
import {login} from './api'

type FormSubmitHandler = NonNullable<ComponentPropsWithoutRef<'form'>['onSubmit']>

type LoginPageProps = {
    onLogin: (token: string) => void
}

function LoginPage({onLogin}: LoginPageProps) {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [isPasswordVisible, setIsPasswordVisible] = useState(false)
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [errorMessage, setErrorMessage] = useState<string | null>(null)

    const handleSubmit: FormSubmitHandler = async (event) => {
        event.preventDefault()

        setIsSubmitting(true)
        setErrorMessage(null)

        try {
            const token = await login(username, password)
            onLogin(token)
        }
        catch (error) {
            setErrorMessage(getLoginErrorMessage(error))
        }
        finally {
            setIsSubmitting(false)
        }
    }

    const loginFormProps = {
        username,
        password,
        isPasswordVisible,
        isSubmitting,
        errorMessage,
        onUsernameChange: setUsername,
        onPasswordChange: setPassword,
        onPasswordVisibilityToggle: () =>
            setIsPasswordVisible((isVisible) => !isVisible),
        onSubmit: handleSubmit,
    }

    return (
        <main className={styles.page} aria-label="Administrator login">
            <DesktopLogin loginFormProps={loginFormProps}/>
            <MobileLogin loginFormProps={loginFormProps}/>
        </main>
    )
}

type LoginShellProps = {
    loginFormProps: LoginFormProps
}

function DesktopLogin({loginFormProps}: LoginShellProps) {
    return (
        <div className={styles.desktopLayout}>
            <aside className={styles.brandPanel}>
                <BrandHeader logoSize={30}/>

                <section className={styles.brandMessage} aria-labelledby="desktop-login-title">
                    <div className={styles.eyebrow}>
                        <Wifi size={14} strokeWidth={2} aria-hidden="true"/>
                        <span>Network Operations</span>
                    </div>
                    <h1 id="desktop-login-title">Wi-Fi CPE Configuration</h1>
                    <p>Manage Wi-Fi parameters for subscriber CPE devices.</p>
                </section>
            </aside>

            <section className={styles.loginArea}>
                <LoginForm {...loginFormProps} />
            </section>
        </div>
    )
}

function MobileLogin({loginFormProps}: LoginShellProps) {
    return (
        <div className={styles.mobileLayout}>
            <header className={styles.mobileHeader}>
                <BrandHeader logoSize={26}/>
                <Wifi className={styles.mobileHeaderIcon} size={14} strokeWidth={2} aria-hidden="true"/>
            </header>

            <section className={styles.mobileContent} aria-labelledby="mobile-login-title">
                <h1 id="mobile-login-title">Wi-Fi CPE Configuration</h1>
                <LoginForm {...loginFormProps} />
            </section>
        </div>
    )
}

type BrandHeaderProps = {
    logoSize: number
}

function BrandHeader({logoSize}: BrandHeaderProps) {
    return (
        <div className={styles.brandHeader}>
            <scale-logo
                className={styles.logo}
                variant="magenta"
                transparent
                size={logoSize}
                language=""
                logo-hide-title
            />
            <span className={styles.brandName}>Hrvatski Telekom</span>
        </div>
    )
}

type LoginFormProps = {
    username: string
    password: string
    isPasswordVisible: boolean
    isSubmitting: boolean
    errorMessage: string | null
    onUsernameChange: (value: string) => void
    onPasswordChange: (value: string) => void
    onPasswordVisibilityToggle: () => void
    onSubmit: FormSubmitHandler
}

function LoginForm({
    username,
    password,
    isPasswordVisible,
    isSubmitting,
    errorMessage,
    onUsernameChange,
    onPasswordChange,
    onPasswordVisibilityToggle,
    onSubmit,
}: LoginFormProps) {
    const usernameId = useId()
    const passwordId = useId()
    const errorId = useId()
    const [isUsernameFocused, setIsUsernameFocused] = useState(false)
    const [isPasswordFocused, setIsPasswordFocused] = useState(false)
    const PasswordIcon = isPasswordVisible ? EyeOff : Eye

    return (
        <form className={styles.form} aria-label="Administrator login form" onSubmit={onSubmit}>
            <div className={styles.formHeader}>
                <h2>Administrator login</h2>
                <p>Sign in to access the Wi-Fi CPE configuration console.</p>
            </div>

            <label className={styles.field} htmlFor={usernameId}>
                <span>Username</span>
                <input
                    id={usernameId}
                    type="text"
                    value={username}
                    placeholder={isUsernameFocused ? '' : 'username'}
                    autoComplete="username"
                    aria-invalid={errorMessage !== null}
                    aria-describedby={errorMessage === null ? undefined : errorId}
                    onFocus={() => setIsUsernameFocused(true)}
                    onBlur={() => setIsUsernameFocused(false)}
                    onChange={(event) => onUsernameChange(event.target.value)}
                />
            </label>

            <label className={styles.field} htmlFor={passwordId}>
                <span>Password</span>
                <div className={styles.passwordInput}>
                    <input
                        id={passwordId}
                        type={isPasswordVisible ? 'text' : 'password'}
                        value={password}
                        placeholder={isPasswordFocused ? '' : 'password'}
                        autoComplete="current-password"
                        aria-invalid={errorMessage !== null}
                        aria-describedby={errorMessage === null ? undefined : errorId}
                        onFocus={() => setIsPasswordFocused(true)}
                        onBlur={() => setIsPasswordFocused(false)}
                        onChange={(event) => onPasswordChange(event.target.value)}
                    />
                    <button
                        type="button"
                        aria-label={isPasswordVisible ? 'Hide password' : 'Show password'}
                        aria-pressed={isPasswordVisible}
                        onClick={onPasswordVisibilityToggle}
                    >
                        <PasswordIcon size={16} strokeWidth={2} aria-hidden="true"/>
                    </button>
                </div>
            </label>

            <button className={styles.submitButton} type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Signing in...' : 'Sign in'}
            </button>

            {errorMessage === null ? null : (
                <p id={errorId} className={styles.errorMessage} role="alert">
                    {errorMessage}
                </p>
            )}
        </form>
    )
}

function getLoginErrorMessage(error: unknown): string {
    if (!(error instanceof Error)) {
        return 'Unable to sign in. Please try again.'
    }
    if (error.name === 'TypeError') {
        return 'Unable to reach the server. Please try again.'
    }
    return error.message
}

export default LoginPage
