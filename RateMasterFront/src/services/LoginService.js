import { createUserWithEmailAndPassword, getAuth, signInWithEmailAndPassword } from "firebase/auth";
import DAOService from '@/services/DAOService';

class LoginService {

    constructor() {
        this.auth = getAuth();
    }

    async login(email, senha) {
        try {
            const userCredential = await signInWithEmailAndPassword(this.auth, email, senha);
            return userCredential.user;
        } catch (error) {
            console.log('erro ao realizar login', error.menssage);
            throw error;
        }
    };

    async createUser(name, userType, email, senha){
        try{
            const userCredential = await createUserWithEmailAndPassword(this.auth, email, senha);
            
            const daoUser = new DAOService('user');
            const daoShop = new DAOService('shop');
            const dao = userType === "individual" ? daoUser : daoShop;

            await dao.insert({
                email: email,
                name: name,
                uid: userCredential.user.uid,
                userType: userType,
            });

            return userCredential;
        }catch(error){
            console.log('Erro ao criar usuario', error.menssage)
            throw error;
        }
    }
}

export default LoginService;
