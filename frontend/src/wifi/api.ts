const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

type LoginResponse = {
    token: string
}

export async function login(
    username: string,
    password: string,
): Promise<string> {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username,
            password,
        }),
    })
    if (!response.ok) {
        throw new Error(await getErrorMessage(response))
    }
    const {token}: LoginResponse = await response.json()

    return token
}

async function getErrorMessage(response: Response): Promise<string> {
    return translateErrorResponse(response.status)
}

function translateErrorResponse(status: number): string {
    if (status === 400) {
        return 'Enter a username and password.'
    }
    if (status === 401) {
        return 'The username or password is incorrect.'
    }
    return 'Unable to sign in. Please try again.'
}
