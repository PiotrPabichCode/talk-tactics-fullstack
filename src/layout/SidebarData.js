import React from "react";
import HomeIcon from "@mui/icons-material/Home";
import AssignmentIcon from "@mui/icons-material/Assignment";
import AccountBoxIcon from "@mui/icons-material/AccountBox";
import TaskIcon from "@mui/icons-material/Task";
import LogoutIcon from "@mui/icons-material/Logout";
import InfoIcon from "@mui/icons-material/Info";

export const SidebarData = [
  {
    title: "Home",
    icon: <HomeIcon />,
    link: "/home",
  },
  {
    title: "Account",
    icon: <AccountBoxIcon />,
    link: "/account",
  },
  {
    title: "Assignment",
    icon: <AssignmentIcon />,
    link: "/assignment",
  },
  {
    title: "Tasks",
    icon: <TaskIcon />,
    link: "/tasks",
  },
  {
    title: "FAQ",
    icon: <InfoIcon />,
    link: "/faq",
  },
  {
    title: "Logout",
    icon: <LogoutIcon />,
    link: "/logout",
  },
];

export default SidebarData;