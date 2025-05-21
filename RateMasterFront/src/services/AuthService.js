import { reactive } from 'vue';
class AuthService {

  static TOKEN_KEY = 'token';

  static login(token) {
    localStorage.setItem(this.TOKEN_KEY, token);
    authState.isLogged = true;
    authState.userRole = this.getUserRole();
    authState.userId = this.getUserId();
  }

  static logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    authState.isLogged = false;
    authState.userRole = null;
    authState.userId = null;
  }

  static getToken() {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  static isLoggedIn() {
    const token = this.getToken();
    if (!token) return false;

    try {
      const payload = this.parseJwt(token);
      const now = Math.floor(Date.now() / 1000);
      if (payload.exp && payload.exp < now) {
        this.logout();
        return false;
      }
      return true;
    } catch {
      return false;
    }
  }

  static parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  }

  static getUserRole() {
    const token = this.getToken();
    if (!token) return null;
    const payload = this.parseJwt(token);
    console.log(payload)
    return payload.role || null;
  }

  static getUserId() {
    const token = this.getToken();
    if (!token) return null;
    const payload = this.parseJwt(token);
    console.log(payload);
    return payload.id || null;
  }
}
const authState = reactive({
  isLogged: AuthService.isLoggedIn(),
  userRole: AuthService.getUserRole(),
  userId: AuthService.getUserId(),
});
export { authState };
export default AuthService;
