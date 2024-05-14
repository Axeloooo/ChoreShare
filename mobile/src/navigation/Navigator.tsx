import { NavigationContainer } from "@react-navigation/native";
import BottomTabNavigator from "./BottomTabNavigator";
import { useAppDispatch, useAppSelector } from "../hooks/store";
import AuthStackNavigator from "./AuthStackNavigator";
import { RootState } from "../redux/store";
import { Auth, setAuth } from "../redux/slices/authSlice";
import { useEffect } from "react";
import { fetchSession } from "../database";

const Navigator = (): React.JSX.Element => {
  const auth: Auth = useAppSelector((state: RootState) => state.auth);

  const dispatch = useAppDispatch();

  const handleFetchSession = async () => {
    try {
      const session = await fetchSession();
      if (session.rows._array.length > 0) {
        dispatch(
          setAuth({
            userId: session.rows._array[0].userId,
            token: session.rows._array[0].token,
          })
        );
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    handleFetchSession();
  }, []);

  return (
    <NavigationContainer>
      {auth.userId && auth.token ? (
        <BottomTabNavigator></BottomTabNavigator>
      ) : (
        <AuthStackNavigator></AuthStackNavigator>
      )}
    </NavigationContainer>
  );
};

export default Navigator;
