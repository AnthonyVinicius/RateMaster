import { reactive } from 'vue';

class AuthService {
  static TOKEN_KEY = 'token';
  static #cachedPayload = null;

  static login(token) {
    localStorage.setItem(this.TOKEN_KEY, token);
    this.clearCache();
    this.updateAuthState();
  }

  static logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    this.clearCache();
    this.updateAuthState();
  }

  static getToken() {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  static clearCache() {
    this.#cachedPayload = null;
  }

  static isLoggedIn() {
    const token = this.getToken();
    if (!token) return false;

    const payload = this.parseJwt(token);
    if (!payload) return false;

    const now = Math.floor(Date.now() / 1000);
    if (payload.exp && payload.exp < now) {
      this.logout();
      return false;
    }

    return true;
  }

  static parseJwt(token) {
    if (this.#cachedPayload) return this.#cachedPayload;

    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      this.#cachedPayload = JSON.parse(jsonPayload);
      return this.#cachedPayload;
    } catch (err) {
      console.error("Erro ao decodificar o token:", err);
      return null;
    }
  }

  static getPayload() {
    const token = this.getToken();
    return token ? this.parseJwt(token) : null;
  }

  static getUserRole() {
    return this.getPayload()?.role || null;
  }

  static getUserId() {
    return this.getPayload()?.id || null;
  }

  static getUserEmail() {
    return this.getPayload()?.sub || null;
  }

  static getUserName() {
    return this.getPayload()?.name || null;
  }

  static updateAuthState() {
    authState.isLogged = this.isLoggedIn();
    authState.userRole = this.getUserRole();
    authState.userId = this.getUserId();
    authState.userEmail = this.getUserEmail();
    authState.userName = this.getUserName();
  }
}

const authState = reactive({
  isLogged: AuthService.isLoggedIn(),
  userRole: AuthService.getUserRole(),
  userId: AuthService.getUserId(),
  userName: AuthService.getUserName(),
  userEmail: AuthService.getUserEmail(),
});

export { authState };
export default AuthService;
