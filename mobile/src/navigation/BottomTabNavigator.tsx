import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { Entypo } from "@expo/vector-icons";
import { FontAwesome5 } from "@expo/vector-icons";
import { Ionicons } from "@expo/vector-icons";
import { FontAwesome } from "@expo/vector-icons";
import ChoresStackNavigator from "./ChoresStackNavigator";
import HomeStackNavigator from "./HomeStackNavigator";
import CalendarStackNavigator from "./CalendarStackNavigator";
import ProfileStackNavigator from "./ProfileStackNavigator";

type BottomTabParamList = {
  Home: undefined;
  Chores: undefined;
  Calendar: undefined;
  Profile: undefined;
};

const Tab = createBottomTabNavigator<BottomTabParamList>();

const BottomTabNavigator = () => {
  return (
    <Tab.Navigator
      initialRouteName="Home"
      screenOptions={{
        headerShown: false,
      }}
    >
      <Tab.Screen
        name="Home"
        component={HomeStackNavigator}
        options={{
          tabBarIcon: ({ focused }) => (
            <Entypo
              name="home"
              size={28}
              color={focused ? "#2f95dc" : "#ccc"}
            />
          ),
        }}
      />
      <Tab.Screen
        name="Chores"
        component={ChoresStackNavigator}
        options={{
          tabBarIcon: ({ focused }) => (
            <FontAwesome
              name="check"
              size={28}
              color={focused ? "#2f95dc" : "#ccc"}
            />
          ),
        }}
      />
      <Tab.Screen
        name="Calendar"
        component={CalendarStackNavigator}
        options={{
          tabBarIcon: ({ focused }) => (
            <Ionicons
              name="calendar-sharp"
              size={24}
              color={focused ? "#2f95dc" : "#ccc"}
            />
          ),
        }}
      />
      <Tab.Screen
        name="Profile"
        component={ProfileStackNavigator}
        options={{
          tabBarIcon: ({ focused }) => (
            <FontAwesome5
              name="user-alt"
              size={24}
              color={focused ? "#2f95dc" : "#ccc"}
            />
          ),
        }}
      />
    </Tab.Navigator>
  );
};

export default BottomTabNavigator;
