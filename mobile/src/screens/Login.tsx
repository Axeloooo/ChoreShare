import {
  Pressable,
  StyleSheet,
  TextInput,
  View,
  Text,
  Button,
} from "react-native";
import { useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { AuthStackParamList } from "../navigation/AuthStackNavigator";

type Props = NativeStackScreenProps<AuthStackParamList, "LoginScreen">;

const Login = ({ route, navigation }: Props) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  return (
    <View style={styles.container}>
      <Text style={styles.title}>ChoreShare</Text>
      <Text style={styles.text}>Already have an account? Login here!</Text>
      <TextInput
        style={styles.input}
        value={username}
        onChangeText={(text) => setUsername(text)}
        placeholder="Username"
        autoCorrect={false}
      ></TextInput>
      <TextInput
        style={styles.input}
        value={password}
        onChangeText={(text) => setPassword(text)}
        autoCorrect={false}
        placeholder="Password"
        secureTextEntry
      ></TextInput>
      <Pressable style={styles.button} onPress={() => console.log(username)}>
        <View>
          <Text style={styles.buttonText}>Login</Text>
        </View>
      </Pressable>
      <Button
        title="Register"
        onPress={() => navigation.navigate("RegisterScreen")}
      ></Button>
    </View>
  );
};

export default Login;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  title: {
    fontSize: 30,
    fontWeight: "bold",
    margin: 12,
  },
  text: {
    fontSize: 16,
    margin: 12,
  },
  input: {
    width: "80%",
    height: 40,
    margin: 12,
    borderWidth: 1,
    padding: 10,
    borderRadius: 5,
    borderColor: "#ccc",
    backgroundColor: "#f8f8f8",
  },
  button: {
    width: "80%",
    height: 40,
    margin: 12,
    backgroundColor: "#3498ff",
    borderRadius: 5,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "#f8f8f8",
    fontSize: 16,
  },
});
