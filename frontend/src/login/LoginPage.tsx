import { useId, useState } from 'react'
import { Eye, EyeOff, Wifi } from 'lucide-react'

import type { ComponentPropsWithoutRef } from 'react'

import styles from './LoginPage.module.css'

type FormSubmitHandler = NonNullable<ComponentPropsWithoutRef<'form'>['onSubmit']>

function LoginPage() {
  const [username, setUsername] = useState('admin')
  const [password, setPassword] = useState('password')
  const [isPasswordVisible, setIsPasswordVisible] = useState(false)

  const handleSubmit: FormSubmitHandler = (event) => {
    event.preventDefault()
  }

  const loginFormProps = {
    username,
    password,
    isPasswordVisible,
    onUsernameChange: setUsername,
    onPasswordChange: setPassword,
    onPasswordVisibilityToggle: () => setIsPasswordVisible((isVisible) => !isVisible),
    onSubmit: handleSubmit,
  }

  return (
    <main className={styles.page} aria-label="Administrator login">
      <DesktopLogin loginFormProps={loginFormProps} />
      <MobileLogin loginFormProps={loginFormProps} />
    </main>
  )
}

type LoginShellProps = {
  loginFormProps: LoginFormProps
}

function DesktopLogin({ loginFormProps }: LoginShellProps) {
  return (
    <div className={styles.desktopLayout}>
      <aside className={styles.brandPanel}>
        <BrandHeader logoSize={30} />

        <section className={styles.brandMessage} aria-labelledby="desktop-login-title">
          <div className={styles.eyebrow}>
            <Wifi size={14} strokeWidth={2} aria-hidden="true" />
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

function MobileLogin({ loginFormProps }: LoginShellProps) {
  return (
    <div className={styles.mobileLayout}>
      <header className={styles.mobileHeader}>
        <BrandHeader logoSize={26} />
        <Wifi className={styles.mobileHeaderIcon} size={14} strokeWidth={2} aria-hidden="true" />
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

function BrandHeader({ logoSize }: BrandHeaderProps) {
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
  onUsernameChange: (value: string) => void
  onPasswordChange: (value: string) => void
  onPasswordVisibilityToggle: () => void
  onSubmit: FormSubmitHandler
}

function LoginForm({
  username,
  password,
  isPasswordVisible,
  onUsernameChange,
  onPasswordChange,
  onPasswordVisibilityToggle,
  onSubmit,
}: LoginFormProps) {
  const usernameId = useId()
  const passwordId = useId()
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
          autoComplete="username"
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
            autoComplete="current-password"
            onChange={(event) => onPasswordChange(event.target.value)}
          />
          <button
            type="button"
            aria-label={isPasswordVisible ? 'Hide password' : 'Show password'}
            aria-pressed={isPasswordVisible}
            onClick={onPasswordVisibilityToggle}
          >
            <PasswordIcon size={16} strokeWidth={2} aria-hidden="true" />
          </button>
        </div>
      </label>

      <button className={styles.submitButton} type="submit">
        Sign in
      </button>
    </form>
  )
}

export default LoginPage
