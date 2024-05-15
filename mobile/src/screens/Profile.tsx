import {
  ActivityIndicator,
  Alert,
  Button,
  StyleSheet,
  Text,
  View,
} from "react-native";
import { deleteSession } from "../database";
import { Auth, clearAuth } from "../redux/slices/authSlice";
import { useAppDispatch, useAppSelector } from "../hooks/store";
import { RootState } from "../redux/store";
import { useEffect, useState } from "react";

type User = {
  id: string;
  firstName: string;
  lastName: string;
  username: string;
  password: string;
  email: string;
  phone: string;
  streak: number;
  missedChores: number;
  createdAt: Date;
  updatedAt: Date;
};

const Profile = (): React.JSX.Element => {
  const [user, setUser] = useState<User | null>(null);

  const [isLoading, setIsLoading] = useState<boolean>(false);

  const auth: Auth = useAppSelector((state: RootState) => state.auth);

  const dispatch = useAppDispatch();

  const handleLogout = async () => {
    if (auth.userId) {
      dispatch(clearAuth());
      await deleteSession(auth.userId);
    }
  };

  const fetchUser = async () => {
    setIsLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8888/api/v1/user/${auth.userId}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${auth.token}`,
          },
        }
      );
      if (!response.ok) {
        throw new Error("Failed to fetch user");
      }
      const data: User = await response.json();
      setUser(data);
      setIsLoading(false);
    } catch (error) {
      Alert.alert("Failed to fetch user", "Please try again.");
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (auth.userId) {
      fetchUser();
    }
  }, [auth.userId]);

  return (
    <View style={styles.container}>
      {isLoading ? (
        <ActivityIndicator size="small" color="#2f95dc" />
      ) : (
        <View>
          <Text>
            Name: {user?.firstName} {user?.lastName}
          </Text>
          <Text>Username: {user?.username}</Text>
          <Text>Email: {user?.email}</Text>
          <Text>Phone: {user?.phone}</Text>
          <Text>Streak: {user?.streak}</Text>
          <Text>Missed Chores: {user?.missedChores}</Text>
        </View>
      )}

      <Button title="Logout" onPress={handleLogout} />
    </View>
  );
};

export default Profile;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  header: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 20,
  },
});
