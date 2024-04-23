import { NavigationContainer } from "@react-navigation/native";
import BottomTabNavigator from "./BottomTabNavigator";
import { useAppSelector } from "../hooks/store";
import AuthStackNavigator from "./AuthStackNavigator";

const Navigator = () => {
  const user = useAppSelector((state) => state.user);

  return (
    <NavigationContainer>
      {user ? (
        <BottomTabNavigator></BottomTabNavigator>
      ) : (
        <AuthStackNavigator></AuthStackNavigator>
      )}
    </NavigationContainer>
  );
};

export default Navigator;
