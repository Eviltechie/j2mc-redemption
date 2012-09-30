--
-- Table structure for table `coupons`
--

CREATE TABLE IF NOT EXISTS `coupons` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` int(12) NOT NULL,
  `creator` varchar(32) NOT NULL,
  `code` int(12) DEFAULT NULL,
  `player` int(32) DEFAULT NULL,
  `redeemed` int(12) DEFAULT NULL,
  `expiry` int(12) DEFAULT NULL,
  `server` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE IF NOT EXISTS `items` (
  `id` int(11) NOT NULL,
  `item` int(4) NOT NULL,
  `quantity` int(5) NOT NULL,
  KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
