import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { getAuth } from "firebase/auth";

const config = {
  apiKey: "AIzaSyCvF3k4n1-Rfckm6W-t8z6y3HKAwfeOWGQ",
  authDomain: "ratemasterif.firebaseapp.com",
  projectId: "ratemasterif",
  storageBucket: "ratemasterif.firebasestorage.app",
  messagingSenderId: "221591868606",
  appId: "1:221591868606:web:e2c1e14662b20c1edffaef",
  measurementId: "G-G8SJ2ZY2G7"
};

const firebaseApp = initializeApp(config);

export const firestore = getFirestore(firebaseApp);
export const auth = getAuth(firebaseApp);
